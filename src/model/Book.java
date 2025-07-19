    package model;

    public class Book {
        private String id;
        private String title;
        private String author;
        private String publicationYear; // Disimpan sebagai String
        private String type; // "Digital" atau "Fisik"
        private String url; // Hanya untuk buku digital
        private boolean isBorrowed; // Status peminjaman
        private String subject;

        // Konstruktor asli yang Anda gunakan sebelumnya
        public Book(String id, String title, String author, String publicationYear, String type, String url) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.publicationYear = publicationYear;
            this.type = type;
            this.url = url != null ? url : "";
            this.isBorrowed = false;
            this.subject = ""; // Default kosong
        }

        // Konstruktor baru yang Anda tambahkan sebelumnya untuk int year, dengan subject
        public Book(String id, String title, String author, int publicationYear, String type, String url, String subject) {
            this.id = id;
            this.title = title;
            this.author = author;
            this.publicationYear = String.valueOf(publicationYear); // Konversi int ke String
            this.type = type;
            this.url = url != null ? url : "";
            this.isBorrowed = false;
            this.subject = subject != null ? subject : "";
        }

        // Getters
        public String getId() { return id; }
        public String getTitle() { return title; }
        public String getAuthor() { return author; }
        public String getPublicationYear() { return publicationYear; }
        public String getType() { return type; }
        public String getUrl() { return url; }
        public String getSubject() { return subject; }
        public boolean isBorrowed() { return isBorrowed; }
        public boolean isDigital() { return "Digital".equals(type); }

        // Setters
        public void setId(String id) { this.id = id; }
        public void setTitle(String title) { this.title = title; }
        public void setAuthor(String author) { this.author = author; }
        public void setPublicationYear(String publicationYear) { this.publicationYear = publicationYear; }
        public void setType(String type) { this.type = type; }
        public void setUrl(String url) { this.url = url; }
        public void setSubject(String subject) { this.subject = subject; }
        public void setBorrowed(boolean borrowed) { isBorrowed = borrowed; }

        @Override
        public String toString() {
            return title;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Book book = (Book) o;
            return id.equals(book.id);
        }

        @Override
        public int hashCode() {
            return id.hashCode();
        }
    }