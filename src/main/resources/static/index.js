$(document).ready(function(){
    //load loans
    $.ajax({
        url: "/api/v1/loans"
    }).then(function(data){
        $("#loanTable").append($("<th><th>"));
        $.each(data.content, function (index, item){
            var row = $('<tr>').append(
                $('<td>').text(index+1),
                $('<td>').text(item.name),
                $('<td>').text(item.rating),
                $('<td>').text(item.amount + " " + item.currency),
                $('<td>').text(item.revenue + "%"),
                $('<td>').text(item.remainingInvestment + " " + item.currency),
                $('<td>').text(item.datePublished)
            );
            row.addClass("cov_" + item.covered);
            $("#loanTable").append(row);
        })
    });

    //load status
    $.ajax({
            url: "/api/v1/status"
    }).then(function(data){
            $("#refreshInterval").append(data.refreshInterval);
            $("#datePublished").append(data.latestDatePublished);
            $("#loansCount").append(data.totalLoansCount);
            $("#defaultSearchRange").append(data.defaultSearchRange);
            $("#additionalAttributes").append(data.additionalAttributes);
    });
});