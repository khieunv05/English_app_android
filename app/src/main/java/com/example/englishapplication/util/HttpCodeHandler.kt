package com.example.englishapplication.util

class HttpCodeHandler {
    companion object{
        fun mapHttpErrorMessage(code: Int): String {
            return when (code) {
                401 -> "Phiên đăng nhập đã hết hạn. Vui lòng đăng nhập lại."
                403 -> "Bạn không có quyền truy cập chức năng này."
                404 -> "Không tìm thấy dữ liệu."
                500, 502, 503 -> "Hệ thống đang gặp sự cố. Vui lòng thử lại sau."
                else -> "Đã có lỗi xảy ra (mã lỗi: $code)."
            }
        }
    }
}