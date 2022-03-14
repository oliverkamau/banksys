$(document).ready(function () {

   userImage("");
   getUser();
   // formatDate();
   updateUser();
})
function clearPage() {
    $('#newUser').click(function () {
        clearData()
    })
}
function formatDate() {
    var d = new Date(),
        month = '' + (d.getMonth() + 1),
        day = '' + d.getDate(),
        year = d.getFullYear();

    if (month.length < 2)
        month = '0' + month;
    if (day.length < 2)
        day = '0' + day;

    $('#date').val([year, month, day].join('-'));

    }


function userImage(url){
    $("#user-avatar").fileinput('destroy').fileinput({
        overwriteInitial: true,
        maxFileSize: 1500,
        showClose: false,
        showCaption: false,
        browseLabel: '',
        removeLabel: '',
        browseIcon: '<i class="fa fa-folder-open"></i>',
        removeIcon: '<i class="fa fa-times"></i>',
        removeTitle: 'Cancel or reset changes',
        elErrorContainer: '#kv-avatar-errors',
        msgErrorClass: 'alert alert-block alert-danger',
        defaultPreviewContent: '<img src="' + url + '"  style="height:13em;width:190px">',
        layoutTemplates: {main2: '{preview} ' + ' {remove} {browse}'},
        allowedFileExtensions: ["jpg", "png", "gif"]
    });

}


function updateUser(){
    $('#saveCustomer').click(function () {
        if($('#saveCustomer').val()==='Edit'){
            $('#saveCustomer').val('Save')
            $('#name').prop('readonly',false);
            $('#gender').prop('disabled',false);
            $('#idNo').prop('readonly',false);;
            $('#pinNo').prop('readonly',false);
            $('#phoneNo').prop('readonly',false);
            $('#date').prop('disabled',false);
            $('#email').prop('readonly',false);
            $('#postalAddress').prop('readonly',false);
            $('#physicalAddress').prop('readonly',false);
        }
        else {
            var form = $('#user-form').serializeArray();
            var formData = new FormData();
            for (var i = 0; i < form.length; i++) {
                formData.append(form[i].name, form[i].value);
            }
            var file = $('#user-avatar')[0].files[0];
            if(typeof file !== "undefined") {
                formData.append('file', $('#user-avatar')[0].files[0])
            }

            $.ajax({
                type: 'POST',
                url: 'updateProfile',
                data: formData,
                processData: false,
                contentType: false
            }).done(function (s) {
                getUser();
                swal({
                    type: 'success',
                    title: 'Success',
                    text: s.success,
                    showConfirmButton: true
                })
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




function getUser() {
    $.ajax({
        type: 'GET',
        url: 'getuser',
        processData: false,
        contentType: false,
    }).done(function (s) {
        $('#saveCustomer').val('Edit');

        $('#userId').val(s.userId);
        $('#name').val(s.name).prop('readonly',true);
        $('#gender').val(s.gender).prop('disabled',true);
        $('#idNo').val(s.idNo).prop('readonly',true);;
        $('#pinNo').val(s.pinNo).prop('readonly',true);
        $('#phoneNo').val(s.phoneNo).prop('readonly',true);
        $('#date').val(s.dob).prop('disabled',true);
        $('#email').val(s.email).prop('readonly',true);
        $('#postalAddress').val(s.postalAddress).prop('readonly',true);
        $('#physicalAddress').val(s.physicalAddress).prop('readonly',true);
        if(s.url) {
            userImage(s.url)
        }


    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    });

}