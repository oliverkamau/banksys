$(document).ready(function () {

    searchAccounts();
    accountChange();
    transactionChange();

})


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


function getAccountTransactions() {
    $.ajax({
        type: 'GET',
        url: '/protected/accounts/getalltransactions/'+$('#transactionAccount').val(),
        processData: false,
        contentType: false,
    }).done(function (s) {
        $('#transTable').DataTable().destroy();
        $("#transTable tbody").empty();
        $.each(s,function(i,item){
            $("#transTable tbody").append(
                "<tr>"
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

function accountChange() {
    $('#account_sel').on('select2:select', function (e) {
        var data = e.params.data;
        $('#transactionAccount').val(data.id)
        getAccountTransactions();
    });
    $("#account_sel").on("select2:unselecting", function(e) {
        $('#transactionAccount').val('')

    });
}
function transactionChange() {
    $('#transaction').on('change', function (e) {

        if($('#transactionAccount').val()===''){
            swal({
                title: 'Alert!',
                type: 'info',
                text: 'Provide an Account first!',
                confirmButtonText: 'OK'
            })
        }
        else{
            getTransactions();
        }
    });

}




function getTransactions(){
    $.ajax({
        type: 'GET',
        url: '/protected/accounts/getspecifictransactions/'+$('#transactionAccount').val()+'/'+$('#transaction').val(),
        processData: false,
        contentType: false,
    }).done(function (s) {
        $('#transTable').DataTable().destroy();
        $("#transTable tbody").empty();
        $.each(s,function(i,item){
            $("#transTable tbody").append(
                "<tr>"
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

