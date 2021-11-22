package carogameservice.service;

import carogameservice.ws.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BanCoService {
	private BanCo banCo;
	private static boolean isXDiTruoc;

	public BanCo khoiTaoBanCo(KhoiTaoBanCoRequest request) {
		Player player = new Player();
		player.getListQuanCo();

		List<QuanCo> quanCoList = new ArrayList<>();
		BanCo banCo = new BanCo();
		// Khởi tạo bàn cờ với quân cờ mặc định theo cột, hàng
		for (int i = 0; i < request.getSoHang(); i++) {
			for (int j = 0; j < request.getSoCot(); j++) {
				// Thêm quân cờ mặc định theo cột, hàng vào bàn cờ
				QuanCo quanCo = new QuanCo();
				quanCo.setHang(i);
				quanCo.setCot(j);
				quanCoList.add(quanCo);
			}
		}
		banCo.setSoHang(request.getSoHang());
		banCo.setSoCot(request.getSoCot());
		banCo.setPlayer(player);
		banCo.getListQuanCo().addAll(quanCoList);
		System.out.println("List quan co: ");
		quanCoList.stream().forEach(c -> System.out.println("Hang: " + c.getHang() + ", cot: " + c.getCot() + ", type: " + c.getType()));
		this.banCo = banCo;
		return banCo;
	}

	public boolean kiemTraThangThua(KiemTraThangRequest request) {
		// Kiểm tra thắng cuộc theo đường chéo
		if (checkWinByDiagonal(request.getCot(), request.getHang(), request.getType())) {
			return true;
			// Kiểm tra thằng cuộc theo hàng ngang
		} else if (checkWinByHorizontal(request.getCot(), request.getHang(), request.getType())) {
			return true;
			// Kiểm tra thắng cuộc theo hàng dọc
		} else if (checkWinByVertical(request.getCot(), request.getHang(), request.getType())) {
			return true;
		}
		// Nếu không thỏa mãn trường hợp thắng nào thì trả về false
		return false;
	}

	private boolean checkWinByVertical(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo hàng dọc
		int count = 0;

		// Đếm số quân cờ theo hàng dọc
		for (int i = row - 4; i <= row + 4; i++) {
			// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
			if (i >= 0 && i < banCo.getSoHang()) {
				// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
				// tương ứng
				int index = col + i * banCo.getSoHang();
				// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
				if (loaiQuan.equals(banCo.getListQuanCo().get(index).getType())) {
					count++;
				} else {
					// Nếu gặp quân cờ không trùng thì gán lại count = 0
					count = 0;
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
		}
		// Nếu không thắng trả về false
		return false;
	}

	private boolean checkWinByHorizontal(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo hàng ngang
		int count = 0;

		// Đếm số quân cờ theo hàng ngang
		for (int i = col - 4; i <= col + 4; i++) {
			// Kiểm tra điều kiện các vị trí cột nằm trong bàn cờ
			if (i >= 0 && i < banCo.getSoCot()) {
				// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
				// tương ứng
				int index = i + row * banCo.getSoCot();
				// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
				if (loaiQuan.equals(banCo.getListQuanCo().get(index).getType())) {
					count++;
				} else {
					// Nếu gặp quân cờ không trùng thì gán lại count = 0
					count = 0;
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
		}
		// Nếu không thắng trả về false
		return false;
	}

	private boolean checkWinByDiagonal(int col, int row, String loaiQuan) {
		// Khai báo biến đếm số quân cờ theo chéo phải
		int count = 0;
		// Khai báo biến chứa giá trị vị trí hàng trừ đi 4
		int j = row - 4;
		// Đếm số quân cờ của đường chéo phải
		for (int i = col - 4; i <= col + 4; i++) {
			// Kiểm tra điều kiện các vị trí cột nằm trong bàn cờ
			if (i >= 0 && i < banCo.getSoCot()) {
				// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
				if (j >= 0 && j < banCo.getSoHang()) {
					// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
					// tương ứng
					int index = i + j * banCo.getSoHang();
					// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
					if (loaiQuan.equals(banCo.getListQuanCo().get(index).getType())) {
						count++;
					} else {
						// Nếu gặp quân cờ không trùng thì gán lại count = 0
						count = 0;
					}
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
			// Tăng vị trí hàng lên 1
			j++;
		}

		// Gán lại giá trị biến đếm số quân cờ theo chéo trái
		count = 0;

		// Gán giá trị biến chứa giá trị vị trí hàng trừ đi 4
		j = row - 4;
		// Đếm số quân cờ của đường chéo trái
		for (int i = col + 4; i >= 0; i--) {
			// Kiểm tra điều kiện vị trí cột nằm trong bàn cờ
			if (i < banCo.getSoCot()) {
				// Kiểm tra điều kiện các vị trí hàng nằm trong bàn cờ
				if (j >= 0 && j < banCo.getSoHang()) {
					// Tính chi số trong List chứa quân cờ trên bàn cờ theo theo vị trí cột, hàng
					// tương ứng
					int index = i + j * banCo.getSoHang();
					// Nếu có quân cờ trùng loại quân thì tăng biến đếm 1 đơn vị
					if (loaiQuan.equals(banCo.getListQuanCo().get(index).getType())) {
						count++;
					} else {
						// Nếu gặp quân cờ không trùng thì gán lại count = 0
						count = 0;
					}
				}
			}
			// Nếu số quân cở đủ 5 quân liên tiếp là 0 hoặc X thì return true
			if (count == 5) {
				return true;
			}
			// Tăng vị trí hàng lên 1
			j++;
		}

		// Nếu không thỏa mãn 2 trường hợp trên thì return false;
		return false;

	}

	public boolean kiemTraHopLe(KiemTraTinhHopLeRequest request) {
		boolean thuocBanCo = checkToaDo(request.getHang(), request.getCot());
		if (!thuocBanCo) {
			return false;
		}
		boolean coTrung = kiemTraCoTrung(request.getHang(), request.getCot(), request.getType());
		if (coTrung) {
			return false;
		}
		boolean soQuanCo = kiemTraSoQuanCo(request.getType());
		if (!soQuanCo) {
			return false;
		}
		return true;
	}

	/**
	 * Kiểm tra tọa độ quân cờ có đánh đúng trong bàn cờ không?
	 *
	 * @return Giá trị sai nếu tọa độ nằm ngoài bàn cờ
	 */
	private boolean checkToaDo(int hang, int cot) {
		// Nếu tọa độ âm thì tọa độ không hợp lệ, trả về false
		if (hang < 0 || cot < 0) {
			return false;
		}

		// Nếu tọa độ x,y nằm trong bàn cờ thì tọa độ đó hợp lệ. Trả về true
		if (cot >= 0 && cot <= banCo.getSoCot()) {
			if (hang >= 0 && hang <= banCo.getSoHang()) {
				return true;
			}
		}
		// Nếu không thỏa mãn thì trả về false
		return false;
	}

	/**
	 * Kiếm tra tọa độ quân cờ đã có trên bàn cờ chưa
	 *
	 * @return Giá trị đúng hoặc sai. Nếu đúng là quân cờ trùng và ngược lại
	 */
	private boolean kiemTraCoTrung(int hang, int cot, String type) {
		Optional<QuanCo> quanCoOpt = banCo.getListQuanCo()
				.stream()
				.filter(c -> c.getHang() == hang && c.getCot() == cot && type.equals(c.getType()))
				.findFirst();
		if (quanCoOpt.isPresent()) {
			return true;
		}
		return false;
	}

	private boolean kiemTraSoQuanCo(String type) {
		long tongQuanCoX = banCo.getListQuanCo()
				.stream()
				.filter(c -> "X".equalsIgnoreCase(c.getType()))
				.count();
		long tongQuanCoO = banCo.getListQuanCo()
				.stream()
				.filter(c -> "O".equalsIgnoreCase(c.getType()))
				.count();
		if (tongQuanCoX == 0 && tongQuanCoO == 0 && "X".equalsIgnoreCase(type)) {
			isXDiTruoc = true;
		} else if (tongQuanCoX == 0 && tongQuanCoO == 0 && "O".equalsIgnoreCase(type)) {
			isXDiTruoc = false;
		}
		// X di truoc
		if (isXDiTruoc) {
			if ("X".equalsIgnoreCase(type)) {
				return tongQuanCoX == tongQuanCoO;
			} else {
				return tongQuanCoX == tongQuanCoO + 1;
			}
		} else {
			if ("O".equalsIgnoreCase(type)) {
				return tongQuanCoO == tongQuanCoX;
			} else {
				return tongQuanCoO == tongQuanCoX + 1;
			}
		}
	}

	public void updateLoaiQuanCo(QuanCo quanCo, String type) {
		if (quanCo != null) {
			quanCo.setType(type);
		}
	}

	public QuanCo getQuanCoByHangCot(int hang, int cot) {
		Optional<QuanCo> optQuanCo = banCo.getListQuanCo().stream()
				.filter(c -> hang == c.getHang() && cot == c.getCot())
				.findFirst();
		return optQuanCo.isPresent() ? optQuanCo.get() : null;
	}
}
