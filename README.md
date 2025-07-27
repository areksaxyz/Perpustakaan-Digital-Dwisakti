🌾 Sistem Irigasi Otomatis

Selamat datang di repositori Sistem Irigasi Otomatis – sebuah aplikasi simulasi real-time berbasis web untuk mengelola irigasi dua sawah (Sawah A dan Sawah B) dengan fitur otomatisasi berdasarkan kelembapan tanah dan jadwal. Aplikasi ini dirancang untuk memberikan pengalaman pengguna yang intuitif dengan antarmuka modern menggunakan React dan Tailwind CSS.



🎯 Fitur Utama





Manajemen Irigasi Manual: Mulai atau hentikan penyiraman secara manual untuk Sawah A dan Sawah B.



Irigasi Otomatis: Penyiraman otomatis berdasarkan ambang kelembapan atau jadwal harian.



Monitoring Real-time: Visualisasi kelembapan tanah dan status pompa untuk setiap sawah.



Riwayat Penyiraman: Log aktivitas penyiraman dengan waktu, status kelembapan, dan aksi.



Notifikasi: Notifikasi real-time untuk peristiwa penting (penyiraman dimulai/dihentikan, kondisi tanah sangat basah).



Pengaturan: Konfigurasi ambang kelembapan, jadwal penyiraman, durasi, dan pengaturan notifikasi.



Visualisasi Sawah: Animasi tetes air dan tingkat air berdasarkan kelembapan tanah.



Cuaca dan Hasil: Simulasi cuaca (cerah, berawan, hujan) dan pelacakan hasil irigasi harian.



Penyimpanan Lokal: Data sawah, riwayat, dan pengaturan disimpan di localStorage untuk persistensi.



🖼️ Penjelasan Antarmuka

1. Beranda (Dashboard)

Menampilkan status kelembapan Sawah A dan Sawah B, visualisasi sawah, status cuaca, hasil irigasi harian, dan grafik total penyiraman.



2. Monitoring

Menampilkan grafik kelembapan tanah untuk sawah yang dipilih, status pompa, dan tren suhu udara (placeholder).



3. Riwayat

Log aktivitas penyiraman, dikelompokkan berdasarkan tanggal, dengan informasi sawah, aksi, dan kelembapan.



4. Notifikasi

Menampilkan notifikasi baru dan sudah dibaca, dengan opsi untuk menandai sebagai dibaca.



5. Pengaturan

Konfigurasi bahasa, unit kelembapan, notifikasi aplikasi, serta pengaturan penyiraman otomatis untuk setiap sawah (aktif/tidak, ambang kelembapan, jadwal, durasi).





⚙️ Teknologi yang Digunakan





Bahasa Pemrograman: JavaScript (ES6+)



Framework UI: React 18



Styling: Tailwind CSS



Transpiler: Babel (untuk JSX)



Penyimpanan Data: localStorage untuk persistensi data



📦 Dependensi Eksternal





React 18 – Framework UI (via CDN)



ReactDOM 18 – Rendering React (via CDN)



Tailwind CSS – Styling (via CDN)



Babel – Transpiler untuk JSX (via CDN)



🔧 Prasyarat





Peramban web modern (Google Chrome, Firefox, atau Edge)



Koneksi internet untuk memuat dependensi CDN (opsional, jika dependensi diunduh secara lokal)



Editor kode (misalnya, VS Code) untuk mengedit atau menjalankan proyek



🚀 Instalasi & Penggunaan

1. Clone Repositori

Buka terminal dan jalankan:

git clone https://github.com/username/sistem-irigasi-otomatis
cd sistem-irigasi-otomatis

2. Menjalankan Aplikasi

Karena aplikasi ini menggunakan CDN untuk dependensi, Anda dapat langsung menjalankannya menggunakan server lokal.

Menggunakan Live Server (VS Code)





Buka proyek di VS Code.



Instal ekstensi Live Server.



Klik kanan pada index.html dan pilih Open with Live Server.

Menggunakan Python Simple HTTP Server

python -m http.server 8000

Buka peramban dan akses http://localhost:8000.

Menggunakan Node.js (opsional, dengan http-server)





Instal http-server secara global:

npm install -g http-server





Jalankan server:

http-server

Buka http://localhost:8080 di peramban.

3. Pengembangan

Untuk mengedit kode:





Buka index.html di editor kode.



Modifikasi file sesuai kebutuhan.



Simpan dan muat ulang peramban untuk melihat perubahan.



📝 Catatan Pengembangan





Persisten Data: A aplikasi menggunakan localStorage untuk menyimpan state (sawah, riwayat, notifikasi, dll.). Pastikan untuk membatasi ukuran riwayat dan notifikasi agar tidak memenuhi penyimpanan.



Simulasi Real-time: Simulasi berjalan menggunakan requestAnimationFrame dengan interval 1 detik = 1 menit simulasi. Sesuaikan SIM_INTERVAL_MS untuk mengubah kecepatan simulasi.



Ekstensi Fitur:





Tambahkan sawah baru dengan memperbarui initialSawahs di kode.



Integrasikan Chart.js untuk grafik yang lebih interaktif.



Tambahkan ekspor riwayat ke CSV (lihat saran di bawah).



Tangkapan Layar: Ganti placeholder [tampilan/*.png] dengan tangkapan layar aktual setelah aplikasi selesai diuji.



🔮 Rencana Pengembangan





Fitur Ekspor Riwayat: Tambahkan tombol untuk mengunduh riwayat penyiraman sebagai file CSV.



Mode Gelap/Terang: Tambahkan opsi tema di pengaturan untuk mendukung mode gelap dan terang.



Integrasi API Cuaca: Gunakan API eksternal untuk data cuaca real-time.



Responsivitas: Optimalkan antarmuka untuk perangkat mobile menggunakan media queries.



🙌 Kontribusi

Kami menyambut kontribusi! Silakan fork repositori ini, buat branch baru, dan ajukan pull request. Pastikan untuk mengikuti pedoman kode dan menambahkan tes jika memungkinkan.





Fork repositori.



Buat branch fitur: git checkout -b fitur-baru.



Commit perubahan: git commit -m "Menambahkan fitur baru".



Push ke branch: git push origin fitur-baru.



Ajukan pull request.



📬 Kontak

Jika Anda memiliki pertanyaan atau saran, silakan hubungi melalui email@example.com atau buka issue di repositori ini.
