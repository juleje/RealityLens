package be.kdg.backendjava.services;

public interface QRCodeService {

    byte[] generateQRCode(String qrContent, int width, int height);
}
