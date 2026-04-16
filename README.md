# Kalkulator Metode Numerik - Java Swing

Aplikasi GUI berbasis Java Swing untuk menghitung berbagai metode numerik. Dibangun dengan arsitektur OOP yang terstruktur.

## Metode yang Tersedia

### 1. Eliminasi Gauss
Metode penyelesaian sistem persamaan linear **Ax = b** secara langsung.

**Cara kerja:**
- Matriks augmented [A|b] direduksi menjadi bentuk segitiga atas melalui operasi baris elementer
- Dilakukan **partial pivoting** (memilih pivot terbesar) untuk menghindari pembagian dengan nol dan meningkatkan stabilitas numerik
- Setelah terbentuk segitiga atas, solusi diperoleh melalui **substitusi balik** dari baris terakhir ke baris pertama

**Input:** Matriks koefisien A (n×n), Vektor konstanta b

**Contoh:**
```
2x + y - z = 8
-3x - y + 2z = -11
-2x + y + 2z = -3
```

---

### 2. Gauss-Seidel
Metode **iteratif** untuk menyelesaikan sistem persamaan linear Ax = b.

**Cara kerja:**
- Dimulai dari tebakan awal x₀
- Setiap iterasi, nilai xᵢ diperbarui menggunakan nilai terbaru yang sudah dihitung pada iterasi yang sama (berbeda dengan Jacobi yang menggunakan nilai iterasi sebelumnya)
- Rumus: `xᵢ = (bᵢ - Σⱼ≠ᵢ aᵢⱼxⱼ) / aᵢᵢ`
- Iterasi berhenti ketika **error** (selisih maksimum antar iterasi) lebih kecil dari toleransi atau mencapai batas iterasi maksimum

**Syarat konvergensi:** Matriks A sebaiknya **dominan diagonal** (|aᵢᵢ| > Σⱼ≠ᵢ |aᵢⱼ|)

**Input:** Matriks A, Vektor b, Tebakan awal x₀, Toleransi, Maks iterasi

---

### 3. Metode Bagi Dua (Bisection)
Metode pencarian **akar persamaan** f(x) = 0 berdasarkan Teorema Nilai Antara.

**Cara kerja:**
- Diberikan interval [a, b] dimana f(a) dan f(b) berbeda tanda (artinya ada akar di antara keduanya)
- Hitung titik tengah c = (a + b) / 2
- Jika f(c) ≈ 0, maka c adalah akar
- Jika f(a) × f(c) < 0, akar berada di [a, c] → perkecil interval ke kiri
- Jika tidak, akar berada di [c, b] → perkecil interval ke kanan
- Ulangi hingga interval cukup kecil (sesuai toleransi)

**Kelebihan:** Selalu konvergen jika syarat tanda terpenuhi
**Kekurangan:** Konvergensi lambat (linear)

**Input:** Fungsi f(x), Batas a dan b, Toleransi, Maks iterasi

---

### 4. Metode Secant
Metode pencarian **akar persamaan** f(x) = 0 tanpa memerlukan turunan (berbeda dengan Newton-Raphson).

**Cara kerja:**
- Dimulai dari dua tebakan awal x₀ dan x₁
- Garis secant ditarik melalui titik (x₀, f(x₀)) dan (x₁, f(x₁))
- Perpotongan garis secant dengan sumbu-x menjadi tebakan baru:
  `x₂ = x₁ - f(x₁) × (x₁ - x₀) / (f(x₁) - f(x₀))`
- Ulangi dengan menggeser: x₀ ← x₁, x₁ ← x₂

**Kelebihan:** Konvergensi lebih cepat dari Bisection (orde ≈ 1.618)
**Kekurangan:** Tidak dijamin konvergen

**Input:** Fungsi f(x), Tebakan x₀ dan x₁, Toleransi, Maks iterasi

---

### 5. Interpolasi Newton (Beda Terbagi)
Metode untuk membangun **polinom interpolasi** dari sekumpulan titik data dan mengestimasi nilai pada titik tertentu.

**Cara kerja:**
- Diberikan n titik data (xᵢ, yᵢ)
- Hitung **tabel beda terbagi** (divided differences):
  - Orde 0: f[xᵢ] = yᵢ
  - Orde 1: f[xᵢ, xᵢ₊₁] = (f[xᵢ₊₁] - f[xᵢ]) / (xᵢ₊₁ - xᵢ)
  - Orde k: rekursif dari orde sebelumnya
- Polinom Newton: `P(x) = a₀ + a₁(x-x₀) + a₂(x-x₀)(x-x₁) + ...`
- Koefisien aᵢ = f[x₀, x₁, ..., xᵢ] (baris pertama tabel beda terbagi)

**Input:** Data x (pisah koma), Data y (pisah koma), Nilai x yang dicari

---

### 6. Metode Trapesium (Trapezoidal Rule)
Metode **integrasi numerik** untuk menghampiri nilai integral tentu ∫ₐᵇ f(x) dx.

**Cara kerja:**
- Interval [a, b] dibagi menjadi n sub-interval dengan lebar h = (b - a) / n
- Kurva f(x) pada setiap sub-interval dihampiri dengan garis lurus (trapesium)
- Rumus komposit:
  `∫ₐᵇ f(x) dx ≈ h × [½f(a) + f(x₁) + f(x₂) + ... + f(xₙ₋₁) + ½f(b)]`
- Semakin besar n, semakin akurat hasilnya

**Orde akurasi:** O(h²)

**Input:** Fungsi f(x), Batas a dan b, Jumlah segmen n

---

### 7. Runge-Kutta Orde 4 (RK4)
Metode numerik untuk menyelesaikan **persamaan diferensial biasa** (ODE) y' = f(x, y).

**Cara kerja:**
- Diberikan kondisi awal (x₀, y₀) dan ukuran langkah h
- Setiap langkah menghitung 4 koefisien:
  - `k₁ = h × f(x, y)`
  - `k₂ = h × f(x + h/2, y + k₁/2)`
  - `k₃ = h × f(x + h/2, y + k₂/2)`
  - `k₄ = h × f(x + h, y + k₃)`
- Nilai y diperbarui: `y = y + (k₁ + 2k₂ + 2k₃ + k₄) / 6`
- x diperbarui: `x = x + h`

**Orde akurasi:** O(h⁴) — sangat akurat untuk ukuran langkah kecil

**Input:** Fungsi f(x,y), Kondisi awal x₀ dan y₀, Ukuran langkah h, Jumlah langkah

---

## Fungsi Matematika yang Didukung

| Fungsi | Contoh |
|--------|--------|
| Penjumlahan, pengurangan | `x + 2`, `x - 3` |
| Perkalian, pembagian | `2*x`, `x/3` |
| Pangkat | `x^2`, `x^3` |
| Sinus, cosinus, tangen | `sin(x)`, `cos(x)`, `tan(x)` |
| Akar kuadrat | `sqrt(x)` |
| Nilai mutlak | `abs(x)` |
| Eksponensial | `exp(x)` |
| Logaritma natural | `ln(x)` |
| Logaritma basis 10 | `log(x)` |
| Konstanta | `pi`, `e` |

---

## Struktur Proyek

```
src/
├── model/          → Data class hasil komputasi
├── parser/         → Parsing ekspresi matematika & input
├── solver/         → Logika komputasi tiap metode numerik
├── ui/             → Utility komponen GUI
├── panel/          → Panel UI untuk setiap metode
└── NumericalMethodsCalculator.java  → Main class
```

## Cara Menjalankan

```bash
cd src
javac -d ../out model/*.java parser/*.java solver/*.java ui/*.java panel/*.java NumericalMethodsCalculator.java
cd ../out
java NumericalMethodsCalculator
```
