package carogameservice.endpoint;

import carogameservice.service.BanCoService;
import carogameservice.ws.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class BanCoEnpoint {
	private static final String NAMESPACE_URI = "http://carogameservice/ws";

	@Autowired
	private BanCoService banCoService;

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "khoiTaoBanCoRequest")
	@ResponsePayload
	public KhoiTaoBanCoResponse khoiTaoBanCo(@RequestPayload KhoiTaoBanCoRequest request) {
		System.out.println("Khoi tao ban co request, so hang: " + request.getSoHang() + ", so cot: " + request.getSoCot());
		KhoiTaoBanCoResponse response = new KhoiTaoBanCoResponse();
		try {
			BanCo banCo = banCoService.khoiTaoBanCo(request);
			if (banCo != null) {
				response.setKetQua(true);
				response.setMoTa("Khoi tao ban co thanh cong!!");
				response.setBanCo(banCo);
			} else {
				response.setKetQua(false);
				response.setMoTa("Khoi tao ban co that bai!!");
			}
		} catch (Exception e) {
			System.out.println("Err " + e);
			response.setKetQua(false);
			response.setMoTa("Co loi xay ra...");
		}
		System.out.println("Khoi tao ban co response, ketqua: " + response.isKetQua() + ", mota: " + response.getMoTa());
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "kiemTraTinhHopLeRequest")
	@ResponsePayload
	public KiemTraTinhHopLeResponse kiemTraHopLe(@RequestPayload KiemTraTinhHopLeRequest request) {
		System.out.println("Kiem tra hop le request, hang: " + request.getHang() + ",cot: " + request.getCot() + ",type: " + request.getType());
		KiemTraTinhHopLeResponse response = new KiemTraTinhHopLeResponse();
		try {
			boolean isHopLe = banCoService.kiemTraHopLe(request);
			QuanCo quanCo = banCoService.getQuanCoByHangCot(request.getHang(), request.getCot());
			response.setQuanCo(quanCo);
			if (isHopLe) {
				response.setKetQua(true);
				response.setMoTa("Nuoc co di hop le!!");
				banCoService.updateLoaiQuanCo(quanCo, request.getType());
				System.out.println("Danh co vao vi tri, hang: " + request.getHang() + ",cot: " + request.getCot() + ", loai: " + request.getType());
			} else {
				response.setKetQua(false);
				response.setMoTa("Nuoc co di khong hop le!!");
			}
		} catch (Exception e) {
			System.out.println("Err " + e);
			response.setKetQua(false);
			response.setMoTa("Co loi xay ra...");
		}
		System.out.println("Kiem tra hop le response, ketqua: " + response.isKetQua() + ", mota: " + response.getMoTa());
		return response;
	}

	@PayloadRoot(namespace = NAMESPACE_URI, localPart = "kiemTraThangRequest")
	@ResponsePayload
	public KiemTraThangResponse kiemTraThang(@RequestPayload KiemTraThangRequest request) {
		System.out.println("Kiem tra thang request, hang: " + request.getHang() + ",cot: " + request.getCot() + ",type: " + request.getType());
		KiemTraThangResponse response = new KiemTraThangResponse();
		try {
			boolean isThang = banCoService.kiemTraThangThua(request);
			if (isThang) {
				response.setKetQua(true);
				response.setMoTa("Cờ " + request.getType() + " thắng!!");
			} else {
				response.setKetQua(false);
				response.setMoTa("Nuoc co " + request.getType() + " chua thang");
			}
			QuanCo quanCo = banCoService.getQuanCoByHangCot(request.getHang(), request.getCot());
			response.setQuanCo(quanCo);
		} catch (Exception e) {
			System.out.println("Err " + e);
			response.setKetQua(false);
			response.setMoTa("Co loi xay ra...");
		}
		System.out.println("Kiem tra thang response, ketqua: " + response.isKetQua() + ", mota: " + response.getMoTa());
		return response;
	}
}
