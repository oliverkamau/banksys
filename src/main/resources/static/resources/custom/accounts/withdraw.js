$(document).ready(function () {

    searchAccounts();
    accountChange();
    amountToWords();
    getCumulative();
    withDrawCash();
    editTransTable();
    newWithDraw();
    getTransactions();

})
function newWithDraw(){
    $('#clear').click(function (){
        clearPage()
    })
}
function clearPage(){

    $('#transactionId').val('')
    $('#balance').text("")
    $('#amount').val('')
    $('#wording').val('')
    $('#account_sel').empty()
    $('#withDrawAccount').val('')
    $('#totalCf').val('')
    $('#balanceCf').val('')
    $('#total').text('')
    $('#transDate').text('')
    $('#remarks').val('')




}

function searchAccounts() {

    $('#account_sel').select2({
        placeholder: 'Accounts',
        allowClear: true,
        width: '65%',
        ajax: {
            delay: 250,
            url: '/protected/accounts/searchaccount',
            data: function (params) {
                console.log("AA", params);
                return {
                    query: params.term,
                    gotoPage: params.page
                }
            },

            processResults: function (data, params) {
                params.page = params.page || 1;
                console.log('data: ', data);
                return {
                    results: data.results
                };
            }

        }
    })

}


function accountChange() {
    $('#account_sel').on('select2:select', function (e) {
        var data = e.params.data;
        $('#withDrawAccount').val(data.id)
        getBalance()
    });
    $("#account_sel").on("select2:unselecting", function(e) {
        $('#withDrawAccount').val('')

    });
}
function getBalance(){
    $.ajax({
        type: 'GET',
        url: 'balance/'+ $('#withDrawAccount').val(),

    }).done(function (s) {
        $('#balance').text(s.balance);
        $('#balanceCf').val(s.balance);
    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    })
}


function getCumulative(){
    $('#amount').on('input',function (){
        if($('#amount').val()===''){
            $('#total').text(0)
            $('#totalCf').val(0)

        }else {
            var p = parseFloat($('#balanceCf').val()) - parseFloat($('#amount').val())
            $('#totalCf').val(p)
            $('#total').text(p)

        }
    })
}


function amountToWords(){
    $('#amount').on('input',function (){
        var words = toWords($('#amount').val())
        $('#wording').val(words)

    })
}

function sendEmail(transactionId) {
    $.ajax({
        type: 'GET',
        url: '/protected/accounts/sendMail/'+ transactionId

    }).done(function (s) {

    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    })
}

function withDrawCash(){
    $('#withdraw').click(function (){

        if($('#withDrawAccount').val()==='') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an Account to withdraw from!',
                confirmButtonText: 'OK'
            })
        }
        else if($('#amount').val()===''){
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an amount to deposit',
                confirmButtonText: 'OK'
            })
        }

        else {
            if ($('#transactionId').val() === '') {

                var form = $("#withdraw-form")[0];
                var data = new FormData(form);

                $.ajax({
                    type: 'POST',
                    url: '/protected/accounts/withDrawMoney',
                    data: data,
                    processData: false,
                    contentType: false
                }).done(function (s) {


                    swal({
                        type: 'success',
                        title: 'Success',
                        text: s.success,
                        showConfirmButton: true
                    })
                    $('#transDate').text(s.date);
                    getBalance()
                    sendEmail(s.transactionId);



                }).fail(function (xhr, error) {
                    swal({
                        type: 'error',
                        title: 'Error!',
                        text: xhr.responseText,
                        showConfirmButton: true

                    })
                })

            }
            else {
                swal({
                    title: 'Alert!',
                    type: 'info',
                    text: 'Editing of financial records is not allowed!',
                    confirmButtonText: 'OK'
                })
            }
        }
    })
}


function getTransactions(){
    $.ajax({
        type: 'GET',
        url: '/protected/accounts/getwithdrawgrid',
        processData: false,
        contentType: false,
    }).done(function (s) {
        $('#transTable').DataTable().destroy();
        $("#transTable tbody").empty();
        $.each(s,function(i,item){
            $("#transTable tbody").append(
                "<tr>"
                +"<td>"+'<form id="editForm" method="post" enctype="multipart/form-data"><input type="hidden" id="edit-float" name="id" value='+item.code+'></form><button class="btn btn-outline-primary btn-sm btn-editfloat" ><i class="fa fa-edit"></button>'
                +"</td>"
                +"<td>"+item.transtype+"</td>"
                +"<td>"+item.account+"</td>"
                +"<td>"+item.amount+"</td>"
                +"<td>"+item.balance+"</td>"
                +"<td>"+item.transcode+"</td>"
                +"<td>"+item.transdate+"</td>"

                +"</tr>" )
        })
        $('#transTable').DataTable();
    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    });
}

function editTransTable(){
    $('#transTable').on('click','.btn-editfloat',function (s) {
        var data=$(this).closest('tr').find('#edit-float').val();
        $.ajax({

            type: 'GET',
            url: '/protected/accounts/geteditfloat/'+data,

        }).done(function (s) {
            $('#transactionId').val(s.code)
            if (s.accountCode) {
                var $newStat = $("<option selected='selected' value='" + s.accountCode + "'>'+s.accountName+'</option>").val(s.accountCode.toString()).text(s.accountName)

                $('#account_sel').append($newStat).trigger('change');
                $('#depositAccount').val(s.accountCode)

            }
            else {
                $('#account_sel').empty();
                $('#depositAccount').val('')

            }


            $('#amount').val(s.amount)
            $('#balance').text(s.balance)
            $('#total').text(s.balance)
            $('#wording').val(s.wording)
            $('#transDate').val(s.date)
            $('#remarks').val(s.remarks)



        })
            .fail(function (xhr, error) {

                swal({
                    type: 'error',
                    title: 'Error!',
                    text: xhr.responseText,
                    showConfirmButton: true

                })

            });
    });

}