/**
 * 基础组件
 */
(function() {
	var Base = function() {
		var _alert = function(msg) {
			var $alertModal = $("#alertModal");
			if ($alertModal.length > 0) {
				$alertModal.modal('show');
				$(".modal-body", $alertModal).html(
						"<p class='alert-content'>" + msg + "</p>");
			} else {
				// Modal not Exist!
				alert(msg);
			}
		};
		
		var _confirm = function(msg, success, error) {
			var $confirmModal = $("#confirmModal");
			if ($confirmModal.length > 0) {
				$confirmModal.modal('show');			
				$(".modal-footer .confirm-yes").unbind('click');
				$(".modal-footer .confirm-no").unbind('click');			
				$(".modal-body", $confirmModal).html(
						"<p class='alert-content'>" + msg + "</p>");
				$(".modal-footer .confirm-yes", $confirmModal).click(function() {
					if (typeof (success) === "function") {
						success.call();
					}
					$confirmModal.modal('hide');
				});
				$(".modal-footer .confirm-no", $confirmModal).click(function() {
					if (typeof (error) === "function") {
						error.call();
					}
					$confirmModal.modal('hide');
				});
			} else {
				// Modal not Exist!
				var right = confirm(msg);
				if (right == true) {
					if (typeof (success) === "function") {
						success.call();
					}
				} else {
					if (typeof (error) === "function") {
						error.call();
					}
				}
			}
		};
		
		// 注册相关函数
		this.alert = _alert;
		this.confirm = _confirm;
			
	}
	//注册window对象
	window.base = new Base();
})(window, jQuery, document);