import math
import os
import wave

SAMPLE_RATE = 44100
CHANNELS = 1
SAMPLE_WIDTH = 2

OUTPUT_DIR = os.path.join(os.path.dirname(__file__), 'generated')
OUTPUT_PATH = os.path.join(OUTPUT_DIR, 'red_lake_chase_loop.wav')


def midi_to_freq(midi):
    return 440.0 * (2.0 ** ((midi - 69) / 12.0))


def sine(t, freq):
    return math.sin(2.0 * math.pi * freq * t)


def saw(t, freq):
    phase = (t * freq) % 1.0
    return 2.0 * phase - 1.0


def square(t, freq):
    return 1.0 if math.sin(2.0 * math.pi * freq * t) >= 0 else -1.0


def envelope(t, attack, decay, sustain, release, total):
    if t < attack:
        return t / max(attack, 0.0001)
    if t < attack + decay:
        return 1.0 - ((t - attack) / max(decay, 0.0001)) * (1.0 - sustain)
    if t < total:
        return sustain
    return max(0.0, sustain * (1.0 - (t - total) / max(release, 0.0001)))


def mix_channels(*layers):
    out = 0.0
    for layer in layers:
        out += layer
    return out


def note_wave(freq, t, voice='saw', shape=0.6):
    if voice == 'saw':
        return saw(t, freq)
    if voice == 'square':
        return square(t, freq)
    return sine(t, freq)


def organ_phrase(start_t, bpm):
    notes = [
        48, 51, 55, 58,
        51, 55, 58, 60,
        48, 51, 55, 58,
        46, 50, 53, 55,
    ]
    step = 60.0 / bpm
    samples = []
    for i, midi in enumerate(notes):
        note_t = start_t + i * step
        freq = midi_to_freq(midi)
        for n in range(int(step * SAMPLE_RATE)):
            t = note_t + n / SAMPLE_RATE
            phase = (t - note_t) / max(step, 1e-6)
            env = 0.35 * (1.0 - abs(phase - 0.5) * 2.0)
            samples.append(0.18 * env * note_wave(freq, t, 'saw', 0.75))
    return samples


def bass_line(start_t, bpm):
    notes = [
        36, 36, 38, 36,
        36, 36, 34, 34,
        36, 36, 38, 36,
        33, 33, 31, 31,
    ]
    step = 60.0 / bpm
    samples = []
    for i, midi in enumerate(notes):
        note_t = start_t + i * step
        freq = midi_to_freq(midi)
        for n in range(int(step * SAMPLE_RATE)):
            t = note_t + n / SAMPLE_RATE
            env = 0.7 if (n / SAMPLE_RATE) < 0.15 else 0.55
            pulse = (0.9 * square(t, freq) + 0.1 * sine(t, freq * 0.5)) * env
            samples.append(0.35 * pulse)
    return samples


def kick(start_t, bpm):
    samples = []
    beat = 60.0 / bpm
    for beat_idx in range(0, 32):
        t0 = start_t + beat_idx * beat
        for n in range(int(beat * SAMPLE_RATE * 0.9)):
            t = t0 + n / SAMPLE_RATE
            dt = max((t - t0), 0.0)
            env = max(0.0, 1.0 - dt / 0.25)
            freq = max(30.0, 55.0 * (1.0 - dt * 10.0))
            samples.append(0.8 * env * sine(t, freq) * (1.0 - dt * 0.8))
    return samples


def hat(start_t, bpm):
    samples = []
    beat = 60.0 / bpm
    for beat_idx in range(0, 64):
        if beat_idx % 2 == 0:
            continue
        t0 = start_t + beat_idx * beat * 0.5
        for n in range(int(beat * SAMPLE_RATE * 0.1)):
            t = t0 + n / SAMPLE_RATE
            noise = (2.0 * (hash(str((beat_idx, n)) + 'hat') % 1000) / 1000.0) - 1.0
            env = max(0.0, 1.0 - (n / (SAMPLE_RATE * 0.1)))
            samples.append(0.12 * noise * env)
    return samples


def render_track(duration_seconds=32.0, bpm=130):
    total_samples = int(duration_seconds * SAMPLE_RATE)
    output = [0.0] * total_samples

    # Layer 1: cursed organ motif
    for sample_index in range(total_samples):
        t = sample_index / SAMPLE_RATE
        organ = 0.0
        for phase in range(0, 16):
            lead_start = phase * (60.0 / bpm)
            if t >= lead_start and t < lead_start + (60.0 / bpm):
                freq = midi_to_freq([48, 51, 55, 58][phase % 4])
                organ += 0.18 * sine(t, freq) * (1.0 - ((t - lead_start) / max((60.0 / bpm), 0.0001)))
        output[sample_index] += organ

    # Layer 2: bass pulse
    for sample_index in range(total_samples):
        t = sample_index / SAMPLE_RATE
        bass = 0.0
        beat = 60.0 / bpm
        cell = int(t / beat)
        note_period = (t % beat)
        freq = midi_to_freq([36, 36, 38, 36, 36, 36, 34, 34][cell % 8])
        if note_period < beat * 0.7:
            bass += 0.25 * square(t, freq)
        output[sample_index] += bass

    # Layer 3: percussive kick + hihat
    for sample_index in range(total_samples):
        t = sample_index / SAMPLE_RATE
        beat = 60.0 / bpm
        beat_pos = t % beat
        kick_amp = 0.0
        if beat_pos < 0.12:
            kick_amp = max(0.0, 1.0 - beat_pos / 0.12)
        output[sample_index] += 0.55 * kick_amp * sine(t, 52.0 + (1.0 - beat_pos / beat) * 70.0)

        if (int(t / (beat * 0.5)) % 2) == 1 and beat_pos < beat * 0.12:
            output[sample_index] += 0.10 * (2.0 * ((sample_index * 17) % 101 / 100.0) - 1.0)

    # Layer 4: low-pass style motion / sci-fi dread
    for sample_index in range(total_samples):
        t = sample_index / SAMPLE_RATE
        sweep = 0.05 * sine(t, 1.5 + 1.0 * math.sin(2.0 * math.pi * 0.15 * t))
        output[sample_index] += 0.10 * sweep * sine(t, 110.0)

    # Simple echo tail to create space
    for sample_index in range(total_samples):
        if sample_index >= 2205:
            output[sample_index] += 0.18 * output[sample_index - 2205]

    # Normalize to avoid clipping
    peak = max(1.0, max(abs(v) for v in output))
    output = [v / peak for v in output]
    return output


def save_wav(samples, path):
    os.makedirs(os.path.dirname(path), exist_ok=True)
    with wave.open(path, 'wb') as wav_file:
        wav_file.setnchannels(CHANNELS)
        wav_file.setsampwidth(SAMPLE_WIDTH)
        wav_file.setframerate(SAMPLE_RATE)
        frames = b''.join(
            int(max(-1.0, min(1.0, s))) * 32767
            for s in samples
        )
        wav_file.writeframes(frames)


def main():
    track = render_track(duration_seconds=32.0, bpm=130)
    save_wav(track, OUTPUT_PATH)
    print(f'Wrote {OUTPUT_PATH}')


if __name__ == '__main__':
    main()
