package spcore.imgui.nodes.types.math;

import java.util.Random;

public class Perlin2D {
    int[] permutationTable;

    public Perlin2D(int seed)
    {
        var rand = new Random(seed);
        permutationTable = new int[256];
        for (int i = 0; i < 256; i++) {
            permutationTable[i] = rand.nextInt(256);
        }
    }


    private double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }

    private double grad(int hash, double x, double y) {
        int h = hash & 15;
        double u = h < 8 ? x : y;
        double v = h < 4 ? y : h == 12 || h == 14 ? x : 0;

        return ((h & 1) == 0 ? u : -u) + ((h & 2) == 0 ? v : -v);
    }

    public float noise(float x, float y, float scale, int detail, float roughness, float lacunarity) {
        float value = 0;
        float amplitude = 1;
        float frequency = 1;

        for (int i = 0; i < detail; i++) {
            value += interpolatedNoise(x * frequency / scale, y * frequency / scale) * amplitude;
            frequency *= lacunarity;
            amplitude *= roughness;
        }

        return value;
    }

    private double interpolatedNoise(double x, double y) {
        int X = (int)x & 255;
        int Y = (int)y & 255;

        x -= (int)x;
        y -= (int)y;

        double u = fade(x);
        double v = fade(y);

        int A = permutationTable[X] + Y;
        int B = permutationTable[X + 1] + Y;

        return lerp(v, lerp(u, grad(permutationTable[A], x, y), grad(permutationTable[B], x - 1, y)),
                lerp(u, grad(permutationTable[A + 1], x, y - 1), grad(permutationTable[B + 1], x - 1, y - 1)));
    }

}
