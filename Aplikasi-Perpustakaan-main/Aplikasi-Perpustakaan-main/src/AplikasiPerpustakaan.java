import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;

// Kelas Buku
class Buku {
    private String judul;
    private String penulis;
    private int stok;

    public Buku(String judul, String penulis, int stok) {
        this.judul = judul;
        this.penulis = penulis;
        this.stok = stok;
    }

    // Getter dan Setter
    public String getJudul() { return judul; }
    public String getPenulis() { return penulis; }
    public int getStok() { return stok; }
    public void setStok(int stok) { this.stok = stok; }

    @Override
    public String toString() {
        return "Judul: " + judul + ", Penulis: " + penulis + ", Stok: " + stok;
    }
}

// Kelas Peminjam
class Peminjam {
    private String nama;
    private String nim;
    private List<String> bukuDipinjam;

    public Peminjam(String nama, String nim) {
        this.nama = nama;
        this.nim = nim;
        this.bukuDipinjam = new ArrayList<>();
    }

    // Getter dan Setter
    public String getNama() { return nama; }
    public String getNim() { return nim; }
    public List<String> getBukuDipinjam() { return bukuDipinjam; }

    public void tambahBukuPinjaman(String judul) {
        bukuDipinjam.add(judul);
    }

    public void kembalikanBuku(String judul) {
        bukuDipinjam.remove(judul);
    }

    @Override
    public String toString() {
        return "Nama: " + nama + ", NIM: " + nim;
    }
}

// Kelas Perpustakaan
class Perpustakaan {
    private List<Buku> daftarBuku;
    private List<Peminjam> daftarPeminjam;
    private List<Map<String, String>> historyPeminjaman;
    private SimpleDateFormat dateFormat;

    public Perpustakaan() {
        daftarBuku = new ArrayList<>();
        daftarPeminjam = new ArrayList<>();
        historyPeminjaman = new ArrayList<>();
        dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        loadData();
    }

    // Method untuk menyimpan data ke file
    private void saveData() {
        try {
            // Simpan data buku
            BufferedWriter bukuWriter = new BufferedWriter(new FileWriter("buku.txt"));
            for (Buku buku : daftarBuku) {
                bukuWriter.write(buku.getJudul() + "|" + buku.getPenulis() + "|" + buku.getStok() + "\n");
            }
            bukuWriter.close();

            // Simpan data peminjam
            BufferedWriter peminjamWriter = new BufferedWriter(new FileWriter("peminjam.txt"));
            for (Peminjam peminjam : daftarPeminjam) {
                peminjamWriter.write(peminjam.getNama() + "|" + peminjam.getNim() + "\n");
            }
            peminjamWriter.close();

            // Simpan history peminjaman
            BufferedWriter historyWriter = new BufferedWriter(new FileWriter("history.txt"));
            for (Map<String, String> history : historyPeminjaman) {
                historyWriter.write(history.get("nim") + "|" + history.get("judul") + "|" + 
                                   history.get("tanggal") + "|" + history.get("status") + "\n");
            }
            historyWriter.close();
        } catch (IOException e) {
            System.out.println("Error menyimpan data: " + e.getMessage());
        }
    }

    // Method untuk memuat data dari file
    private void loadData() {
        try {
            // Load data buku
            File bukuFile = new File("buku.txt");
            if (bukuFile.exists()) {
                BufferedReader bukuReader = new BufferedReader(new FileReader(bukuFile));
                String line;
                while ((line = bukuReader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 3) {
                        daftarBuku.add(new Buku(parts[0], parts[1], Integer.parseInt(parts[2])));
                    }
                }
                bukuReader.close();
            }

            // Load data peminjam
            File peminjamFile = new File("peminjam.txt");
            if (peminjamFile.exists()) {
                BufferedReader peminjamReader = new BufferedReader(new FileReader(peminjamFile));
                String line;
                while ((line = peminjamReader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 2) {
                        daftarPeminjam.add(new Peminjam(parts[0], parts[1]));
                    }
                }
                peminjamReader.close();
            }

            // Load history peminjaman
            File historyFile = new File("history.txt");
            if (historyFile.exists()) {
                BufferedReader historyReader = new BufferedReader(new FileReader(historyFile));
                String line;
                while ((line = historyReader.readLine()) != null) {
                    String[] parts = line.split("\\|");
                    if (parts.length == 4) {
                        Map<String, String> history = new HashMap<>();
                        history.put("nim", parts[0]);
                        history.put("judul", parts[1]);
                        history.put("tanggal", parts[2]);
                        history.put("status", parts[3]);
                        historyPeminjaman.add(history);
                    }
                }
                historyReader.close();
            }
        } catch (IOException e) {
            System.out.println("Error memuat data: " + e.getMessage());
        }
    }

    // Method untuk menambahkan buku baru
    public void tambahBuku() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("\n--- Tambah Buku Baru ---");
        
        System.out.print("Judul buku: ");
        String judul = scanner.nextLine();
        
        System.out.print("Nama penulis: ");
        String penulis = scanner.nextLine();
        
        System.out.print("Jumlah stok: ");
        int stok = Integer.parseInt(scanner.nextLine());
        
        daftarBuku.add(new Buku(judul, penulis, stok));
        saveData();
        System.out.println("Buku '" + judul + "' berhasil ditambahkan!");
    }

    // Method untuk menampilkan daftar buku
    public void daftarBukuTersedia() {
        System.out.println("\n--- Daftar Buku Tersedia ---");
        if (daftarBuku.isEmpty()) {
            System.out.println("Tidak ada buku tersedia.");
            return;
        }
        
        for (int i = 0; i < daftarBuku.size(); i++) {
            System.out.println((i+1) + ". " + daftarBuku.get(i));
        }
    }

    // Method untuk mencari buku berdasarkan judul
    private Buku cariBuku(String judul) {
        for (Buku buku : daftarBuku) {
            if (buku.getJudul().equalsIgnoreCase(judul)) {
                return buku;
            }
        }
        return null;
    }

    // Method untuk mencari peminjam berdasarkan NIM
    private Peminjam cariPeminjam(String nim) {
        for (Peminjam peminjam : daftarPeminjam) {
            if (peminjam.getNim().equals(nim)) {
                return peminjam;
            }
        }
        return null;
    }

    // Method untuk peminjaman buku
    public void pinjamBuku() {
        System.out.println("\n--- Peminjaman Buku ---");
        daftarBukuTersedia();
        if (daftarBuku.isEmpty()) {
            return;
        }

        Scanner scanner = new Scanner(System.in);
        System.out.print("\nPilih nomor buku yang ingin dipinjam: ");
        int bukuIdx = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (bukuIdx < 0 || bukuIdx >= daftarBuku.size()) {
            System.out.println("Nomor buku tidak valid!");
            return;
        }

        Buku buku = daftarBuku.get(bukuIdx);
        if (buku.getStok() <= 0) {
            System.out.println("Stok buku habis!");
            return;
        }

        System.out.println("\nData Peminjam");
        System.out.print("Nama peminjam: ");
        String nama = scanner.nextLine();
        
        System.out.print("NIM: ");
        String nim = scanner.nextLine();

        // Cari atau tambahkan peminjam baru
        Peminjam peminjam = cariPeminjam(nim);
        if (peminjam == null) {
            peminjam = new Peminjam(nama, nim);
            daftarPeminjam.add(peminjam);
        }

        // Tambahkan ke history peminjaman
        Map<String, String> history = new HashMap<>();
        history.put("nim", nim);
        history.put("judul", buku.getJudul());
        history.put("tanggal", dateFormat.format(new Date()));
        history.put("status", "Dipinjam");
        historyPeminjaman.add(history);

        // Kurangi stok dan tambahkan ke daftar pinjaman
        buku.setStok(buku.getStok() - 1);
        peminjam.tambahBukuPinjaman(buku.getJudul());
        saveData();
        System.out.println("Buku '" + buku.getJudul() + "' berhasil dipinjam oleh " + nama);
    }

    // Method untuk pengembalian buku
    public void kembalikanBuku() {
        System.out.println("\n--- Pengembalian Buku ---");
        Scanner scanner = new Scanner(System.in);
        
        System.out.print("Masukkan NIM peminjam: ");
        String nim = scanner.nextLine();
        
        Peminjam peminjam = cariPeminjam(nim);
        if (peminjam == null) {
            System.out.println("Peminjam tidak ditemukan!");
            return;
        }

        if (peminjam.getBukuDipinjam().isEmpty()) {
            System.out.println("Tidak ada buku yang dipinjam.");
            return;
        }

        System.out.println("\nBuku yang dipinjam:");
        List<String> bukuDipinjam = peminjam.getBukuDipinjam();
        for (int i = 0; i < bukuDipinjam.size(); i++) {
            System.out.println((i+1) + ". " + bukuDipinjam.get(i));
        }

        System.out.print("Pilih nomor buku yang dikembalikan: ");
        int bukuIdx = Integer.parseInt(scanner.nextLine()) - 1;
        
        if (bukuIdx < 0 || bukuIdx >= bukuDipinjam.size()) {
            System.out.println("Nomor buku tidak valid!");
            return;
        }

        String judul = bukuDipinjam.get(bukuIdx);
        Buku buku = cariBuku(judul);
        if (buku != null) {
            buku.setStok(buku.getStok() + 1);
        }

        // Update history peminjaman
        Map<String, String> history = new HashMap<>();
        history.put("nim", nim);
        history.put("judul", judul);
        history.put("tanggal", dateFormat.format(new Date()));
        history.put("status", "Dikembalikan");
        historyPeminjaman.add(history);

        peminjam.kembalikanBuku(judul);
        saveData();
        System.out.println("Buku '" + judul + "' berhasil dikembalikan!");
    }

    // Method untuk menampilkan laporan peminjaman
    public void laporanPeminjaman() {
        System.out.println("\n--- Laporan Peminjaman ---");
        if (historyPeminjaman.isEmpty()) {
            System.out.println("Belum ada data peminjaman.");
            return;
        }

        // Urutkan berdasarkan tanggal terbaru
        historyPeminjaman.sort((h1, h2) -> h2.get("tanggal").compareTo(h1.get("tanggal")));
        
        for (Map<String, String> history : historyPeminjaman) {
            System.out.printf("%s | NIM: %s | Buku: %s | Status: %s\n",
                    history.get("tanggal"),
                    history.get("nim"),
                    history.get("judul"),
                    history.get("status"));
        }
    }

    // Method untuk menampilkan menu utama
    public void menuUtama() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("\n=== Sistem Perpustakaan ===");
            System.out.println("1. Tambah Buku");
            System.out.println("2. Daftar Buku Tersedia");
            System.out.println("3. Daftar Peminjam");
            System.out.println("4. Pinjam Buku");
            System.out.println("5. Kembalikan Buku");
            System.out.println("6. Laporan Peminjaman");
            System.out.println("0. Keluar");
            
            System.out.print("Pilih menu: ");
            String pilihan = scanner.nextLine();

            switch (pilihan) {
                case "1":
                    tambahBuku();
                    break;
                case "2":
                    daftarBukuTersedia();
                    break;
                case "3":
                    daftarPeminjam();
                    break;
                case "4":
                    pinjamBuku();
                    break;
                case "5":
                    kembalikanBuku();
                    break;
                case "6":
                    laporanPeminjaman();
                    break;
                case "0":
                    System.out.println("Terima kasih telah menggunakan sistem perpustakaan.");
                    saveData();
                    return;
                default:
                    System.out.println("Pilihan tidak valid!");
            }
        }
    }

    // Method untuk menampilkan daftar peminjam
    private void daftarPeminjam() {
        System.out.println("\n--- Daftar Peminjam ---");
        if (daftarPeminjam.isEmpty()) {
            System.out.println("Belum ada peminjam.");
            return;
        }
        
        for (int i = 0; i < daftarPeminjam.size(); i++) {
            System.out.println((i+1) + ". " + daftarPeminjam.get(i));
        }
    }
}

// Main Class
public class AplikasiPerpustakaan {
    public static void main(String[] args) {
        Perpustakaan perpustakaan = new Perpustakaan();
        perpustakaan.menuUtama();
    }
}
