$(document).ready(function () {

    searchDebitAccounts();
    accountDebitChange();
    searchCreditAccounts();
    accountCreditChange();
    amountToWords();
    transferCash();
    newTransfer();

})
function newTransfer(){
    $('#clear').click(function (){
        clearPage()
    })
}
function clearPage(){

    $('#transactionId').val('')
    $('#debitbalance').text("")
    $('#creditbalance').text("")
    $('#amount').val('')
    $('#wording').val('')
    $('#account_debit_sel').empty()
    $('#account_credit_sel').empty()
    $('#creditAccount').val('')
    $('#debitAccount').val('')
    $('#creditbalanceCf').val('')
    $('#debitbalanceCf').val('')




}

function searchDebitAccounts() {

    $('#account_debit_sel').select2({
        placeholder: 'Accounts',
        allowClear: true,
        width: '65%',
        ajax: {
            delay: 250,
            url: '/protected/accounts/searchDebitaccount',
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


function accountDebitChange() {
    $('#account_debit_sel').on('select2:select', function (e) {
        var data = e.params.data;
        $('#debitAccount').val(data.id)
        getDebitBalance()
    });
    $("#account_debit_sel").on("select2:unselecting", function(e) {
        $('#debitAccount').val('')

    });
}

function searchCreditAccounts() {

    $('#account_credit_sel').select2({
        placeholder: 'Accounts',
        allowClear: true,
        width: '65%',
        ajax: {
            delay: 250,
            url: '/protected/accounts/searchCreditaccount',
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


function accountCreditChange() {
    $('#account_credit_sel').on('select2:select', function (e) {

        var data = e.params.data;
        if($('#debitAccount').val()===data.id){
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an different Account to transfer from!',
                confirmButtonText: 'OK'
            })
        }
        else {
            $('#creditAccount').val(data.id)
            // getCreditBalance()
        }
    });
    $("#account_credit_sel").on("select2:unselecting", function(e) {
        $('#creditAccount').val('')

    });
}
function getCreditBalance(){
    $.ajax({
        type: 'GET',
        url: 'getcreditbalance/'+ $('#creditAccount').val(),

    }).done(function (s) {
        $('#creditbalance').text(s.balance);
        $('#creditbalanceCf').val(s.balance);
    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
    })
}
function getDebitBalance(){
    $.ajax({
        type: 'GET',
        url: 'getdebitbalance/'+ $('#debitAccount').val(),

    }).done(function (s) {
        $('#debitbalance').text(s.balance);
        $('#debitbalanceCf').val(s.balance);
    }).fail(function (xhr, error) {
        swal({
            type: 'error',
            title: 'Error!',
            text: xhr.responseText,
            showConfirmButton: true

        })
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

function transferCash(){
    $('#transfer').click(function (){

        if($('#debitAccount').val()==='') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an Account to transfer from!',
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
        else if($('#creditAccount').val()==='') {
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an Account to transfer To!',
                confirmButtonText: 'OK'
            })
        }

        else {

                var form = $("#transfer-form")[0];
                var data = new FormData(form);

                $.ajax({
                    type: 'POST',
                    url: '/protected/accounts/transferMoney',
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
                    getCreditBalance();
                    getDebitBalance();
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


    })
}




