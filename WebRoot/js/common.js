function _change() {
	$("#vCode").attr("src", "/goodss/VerifyCodeServlet?" + new Date().getTime());
}