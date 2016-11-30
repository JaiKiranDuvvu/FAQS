/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
$(document).ready(function () {
    function ajaxindicatorstart(text)
    {
        if (jQuery('body').find('#resultLoading').attr('id') != 'resultLoading') {
            jQuery('body').append('<div id="resultLoading" style="display:none"><div><img src="images/ajax-loader.gif"><div>' + text + '</div></div><div class="bg"></div></div>');
        }

        jQuery('#resultLoading').css({
            'width': '100%',
            'height': '100%',
            'position': 'fixed',
            'z-index': '10000000',
            'top': '0',
            'left': '0',
            'right': '0',
            'bottom': '0',
            'margin': 'auto'
        });

        jQuery('#resultLoading .bg').css({
            'background': '#000000',
            'opacity': '0.7',
            'width': '100%',
            'height': '100%',
            'position': 'absolute',
            'top': '0'
        });

        jQuery('#resultLoading>div:first').css({
            'width': '250px',
            'height': '75px',
            'text-align': 'center',
            'position': 'fixed',
            'top': '0',
            'left': '0',
            'right': '0',
            'bottom': '0',
            'margin': 'auto',
            'font-size': '16px',
            'z-index': '10',
            'color': '#ffffff'

        });

        jQuery('#resultLoading .bg').height('100%');
        jQuery('#resultLoading').fadeIn(300);
        jQuery('body').css('cursor', 'wait');
    }
    function ajaxindicatorstop()
    {
        jQuery('#resultLoading .bg').height('100%');
        jQuery('#resultLoading').fadeOut(300);
        jQuery('body').css('cursor', 'default');
    }
    jQuery(document).ajaxStart(function () {
        //show ajax indicator
        ajaxindicatorstart('loading data.. please wait..');
    }).ajaxStop(function () {
        //hide ajax indicator
        ajaxindicatorstop();
    });
    $("#ajaxform").submit(function (e)
    {
        var postData = $(this).serializeArray();
        var formURL = $(this).attr("action");
        $.ajax({
            url: formURL,
            type: "POST",
            data: postData,
            success: function (data, textStatus, jqXHR)
            {
                //data: return data from server
                $('#relaxedQuery').removeClass('hide');
                $('#relaxedQueryOutput').text(jqXHR.getResponseHeader('relaxed_query'));
                $('#result').removeClass('hide');
                $('#resultOutput').text(jqXHR.getResponseHeader('result'));
                $('#rules').removeClass('hide');
                var rules = jqXHR.getResponseHeader('rulesoutput');
                rules = rules.split(',');
                var rulesString = '<ol>';
                jQuery.each(rules, function (i, val) {
                    rulesString = rulesString + '<li>' + val + '</li>';
                });
                rulesString = rulesString + '</ol>'
                $('#rulesOutput').html(rulesString);
            },
            error: function (jqXHR, textStatus, errorThrown)
            {
                alert('Error in fetching data from the server');
            }
        });
        e.preventDefault(); //STOP default action
        //e.unbind(); //unbind. to stop multiple form submit.
    });
    $('#submitButton').on('ckick', function () {
        $("#ajaxform").submit(); //Submit  the FORM
    });

});

