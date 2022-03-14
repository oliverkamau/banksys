$(document).ready(function () {

    getAccounts();
    editAccount();
    saveAccount();
    newAccount();
    setAccountName();
})
function setAccountName() {
    $('#accountType').change(function () {

        $('#accountName').val('');
        var type = $('#accountType').val();
        var username = $('#username').val();
        var text = username+" 's"+" "+type;

        $('#accountName').val(text.toUpperCase());

    })
}




function newAccount() {
   $('#newAccount').click(function () {

       $('#accountId').val('');
       $('#accountName').val('');
       $('#accountNumber').val('');
       $('#accountType').val('');
       $('#description').val('');
       $('#createdon').text('');
       $('#status').text('');
       $('#balance').text('');

   })

}
function saveAccount(){
    $('#saveAccount').click(function () {
        if ($('#accountType').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Account Type Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#accountName').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Email Field is required!',
                confirmButtonText: 'OK'
            })
        } else if ($('#description').val() === '') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Brief Description Field is required!',
                confirmButtonText: 'OK'
            })
        }
        else {
            var form = $('#account-form').serializeArray();
            var formData = new FormData();
            for (var i = 0; i < form.length; i++) {
                formData.append(form[i].name, form[i].value);
            }
            $.ajax({
                type: 'POST',
                url: '/protected/accounts/createAccount',
                data: formData,
                processData: false,
                contentType: false
            }).done(function (s) {
                getAccounts();
                swal({
                    type: 'success',
                    title: 'Success',
                    text: s.success,
                    showConfirmButton: true
                })
                $('#accountId').val(s.accountId);
                $('#accountName').val(s.accountName);
                $('#accountNumber').val(s.accountNumber);
                $('#accountType').val(s.accountType);
                $('#description').val(s.description);
                $('#createdon').text(s.dateCreated);
                $('#status').text(s.accountStatus);
                $('#balance').text(s.accountBalance);
            }).fail(function (xhr, error) {
                swal({
                    type: 'error',
                    title: 'Error!',
                    text: xhr.responseText,
                    showConfirmButton: true

                })
            })

        }


    })
}

function editAccount(){
    $('#accountTable').on('click','.btn-editAccount',function (s) {
        var data=$(this).closest('tr').find('#edit-account').val();
        $.ajax({
            type: 'GET',
            url: '/protected/accounts/editAccount/'+data,
            processData: false,
            contentType: false,
        }).done(function (s) {
            $('#accountId').val(s.accountId);
            $('#accountName').val(s.accountName);
            $('#accountNumber').val(s.accountNumber);
            $('#accountType').val(s.accountType);
            $('#description').val(s.description);
            $('#createdon').text(s.dateCreated);
            $('#status').text(s.accountStatus);
            $('#balance').text(s.accountBalance);

        }).fail(function (xhr, error) {
            swal({
                type: 'error',
                title: 'Error!',
                text: xhr.responseText,
                showConfirmButton: true

            })
        });
    });

}


function getAccounts() {
    $.ajax({
        type: 'GET',
        url: '/protected/accounts/getAccounts',
        processData: false,
        contentType: false,
    }).done(function (s) {
        $('#accountTable').DataTable().destroy();
        $("#accountTable tbody").empty();
        if(s.length!==0) {
            $.each(s, function (i, item) {
                $("#accountTable tbody").append(
                    "<tr scope='col'>"
                    + "<td>" + item.accountType + "</td>"
                    + "<td>" + item.accountName + "</td>"
                    + "<td>" + item.accountNumber + "</td>"
                    + "<td>" + item.accountStatus + "</td>"
                    + "<td>" + item.accountBalance + "</td>"
                    + "<td>" + '<form id="editForm" method="post" enctype="multipart/form-data"><input type="hidden" id="edit-account" name="id" value=' + item.accountId + '></form><button class="btn btn-outline-primary btn-sm btn-editAccount" ><i class="fa fa-edit"></button>'
                    + "</td>"
                    + "</tr>")
            })
        }
        $('#accountTable').DataTable();
    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    });

}