function bindEvents(){
    $('#register').on('click', registerClick);
}

function registerClick(){
	$('#register-modal').modal('show');
}

function updateClick(name, description, price){
	$('#update-modal').modal('show');
	$('#update-name').val(name);
	$('#update-description').val(description);
	$('#update-price').val(price);
}

function updateDiscountClick(name){
	$('#update-discount-modal').modal('show');
	$('#update-name').val(name);
}
