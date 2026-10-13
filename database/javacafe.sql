-- phpMyAdmin SQL Dump
-- version 5.2.1
-- https://www.phpmyadmin.net/
--
-- Máy chủ: 127.0.0.1
-- Thời gian đã tạo: Th10 13, 2026 lúc 12:05 PM
-- Phiên bản máy phục vụ: 10.4.32-MariaDB
-- Phiên bản PHP: 8.2.12

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Cơ sở dữ liệu: `javacafe`
--

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `ban`
--

CREATE TABLE `ban` (
  `Ma` int(50) NOT NULL,
  `TenBan` varchar(50) NOT NULL,
  `TrangThai` varchar(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `ban`
--


-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `chiphi`
--

CREATE TABLE `chiphi` (
  `Ma` int(11) NOT NULL,
  `Thang` int(11) NOT NULL,
  `Nam` int(11) NOT NULL,
  `SoTien` decimal(18,3) NOT NULL,
  `GhiChu` varchar(255) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `hoadon`
--

CREATE TABLE `hoadon` (
  `Ma` int(50) NOT NULL,
  `TenBan` varchar(50) NOT NULL,
  `NgayLap` datetime DEFAULT NULL,
  `TongTien` decimal(15,2) NOT NULL DEFAULT 0.00,
  `GiamGia` decimal(15,2) NOT NULL DEFAULT 0.00,
  `ThanhTien` decimal(15,2) NOT NULL DEFAULT 0.00,
  `TienKhach` decimal(15,2) NOT NULL DEFAULT 0.00,
  `TienThua` decimal(15,2) NOT NULL DEFAULT 0.00,
  `TrangThai` varchar(50) NOT NULL DEFAULT 'Đã Thanh Toán'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `lichdangnhap`
--

CREATE TABLE `lichdangnhap` (
  `ID` int(50) DEFAULT NULL,
  `TaiKhoan` varchar(100) NOT NULL,
  `Quyen` int(100) NOT NULL,
  `ThoiGianVao` datetime NOT NULL,
  `ThoiGianRa` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `lichdangnhap`
--



--
-- Cấu trúc bảng cho bảng `lichsugia`
--

CREATE TABLE `lichsugia` (
  `MaHH` char(50) NOT NULL,
  `NgayHL` date NOT NULL,
  `DonGia` decimal(18,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `loaisanpham`
--

CREATE TABLE `loaisanpham` (
  `Ma` int(50) NOT NULL,
  `TenLoai` varchar(50) NOT NULL,
  `TrangThai` int(50) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `luong`
--

CREATE TABLE `luong` (
  `MaNV` int(11) NOT NULL,
  `TenNV` varchar(100) NOT NULL,
  `Thang` varchar(20) NOT NULL,
  `Luong` int(255) NOT NULL,
  `SoCa` int(11) NOT NULL,
  `TongLuong` int(255) NOT NULL,
  `GhiChu` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `nguyenlieu`
--

CREATE TABLE `nguyenlieu` (
  `MaNL` varchar(20) NOT NULL,
  `TenNL` varchar(100) NOT NULL,
  `NgayNhap` date NOT NULL,
  `Gia` int(11) NOT NULL,
  `GhiChu` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `order_chitiet`
--

CREATE TABLE `order_chitiet` (
  `id` int(50) NOT NULL,
  `MaBan` int(50) NOT NULL,
  `Tensp` varchar(100) NOT NULL,
  `SoLuong` int(100) NOT NULL,
  `Dongia` double(18,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `sanpham`
--

CREATE TABLE `sanpham` (
  `Masp` varchar(50) NOT NULL,
  `Tensp` varchar(200) NOT NULL,
  `Dongia` decimal(18,3) NOT NULL,
  `khuyenmai` varchar(200) NOT NULL,
  `trangthai` varchar(200) NOT NULL,
  `Loaisanpham` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Cấu trúc bảng cho bảng `taikhoan`
--

CREATE TABLE `taikhoan` (
  `Ma` int(50) NOT NULL,
  `MatKhau` char(100) NOT NULL,
  `Ten` varchar(100) NOT NULL,
  `MaCCCD` varchar(20) NOT NULL,
  `QueQuan` varchar(200) NOT NULL,
  `DienThoai` varchar(11) NOT NULL,
  `DiaChi` varchar(200) NOT NULL,
  `Quyen` int(100) NOT NULL,
  `TrangThai` int(200) NOT NULL,
  `Luong` decimal(18,3) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Đang đổ dữ liệu cho bảng `taikhoan`
--


--
-- Chỉ mục cho các bảng đã đổ
--

--
-- Chỉ mục cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  ADD PRIMARY KEY (`Ma`);

--
-- Chỉ mục cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  ADD PRIMARY KEY (`Ma`);

--
-- AUTO_INCREMENT cho các bảng đã đổ
--

--
-- AUTO_INCREMENT cho bảng `hoadon`
--
ALTER TABLE `hoadon`
  MODIFY `Ma` int(50) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT cho bảng `loaisanpham`
--
ALTER TABLE `loaisanpham`
  MODIFY `Ma` int(50) NOT NULL AUTO_INCREMENT;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
