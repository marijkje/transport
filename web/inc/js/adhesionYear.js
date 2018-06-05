$( function() {
    var oDate = new Date();
    oDate.setYear(2017);
    oDate.setMonth(0);
    oDate.setDate(1);
    jQuery.datetimepicker.setLocale('fr');
    jQuery('#adhesionyear').datetimepicker({
                                timepicker: false,
//                                format: 'd/m/Y',
                                format: 'Y',
                                defaultDate: oDate
    });        
} );
