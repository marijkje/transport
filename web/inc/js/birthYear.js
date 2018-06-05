$( function() {
    var oDate = new Date();
    oDate.setYear(1980);
//    oDate.setMonth(0);
//    oDate.setDate(1);
    jQuery.datetimepicker.setLocale('fr');
    jQuery('#birthyear').datetimepicker({
                                timepicker: false,
//                                format: 'd/m/Y',
                                format: 'YYYY',
ShortDatePattern = "YYYY",                                defaultDate: oDate
    });        
} );
