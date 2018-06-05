jQuery( function() {
    var mDate = new Date();
    var dDate = new Date();
    mDate.setDate(mDate.getDate() + 1);
    dDate.setDate(dDate.getDate() + 7);
    jQuery.datetimepicker.setLocale('fr');
    jQuery('#StartDate').datetimepicker({
                                format: 'd/m/Y H:i',
                                minDate: mDate,
                                defaultDate: dDate,
                                defaultTime:'08:00',
                                allowTimes:[
                                '08:00',    
                                '09:00',
                                '10:00',
                                '11:00',
                                '12:00',
                                '13:00',
                                '14:00',
                                '15:00',
                                '16:00',
                                '17:00',
                                '18:00',
                                '19:00',
                                '20:00',
                                '21:00'
                                ]

    });        
    jQuery('#EndDate').datetimepicker({
                                format: 'd/m/Y H:i',
                                minDate: mDate,
                                defaultDate: dDate,
                                defaultTime:'08:00',
                                allowTimes:[
                                '08:00',    
                                '09:00',
                                '10:00',
                                '11:00',
                                '12:00',
                                '13:00',
                                '14:00',
                                '15:00',
                                '16:00',
                                '17:00',
                                '18:00',
                                '19:00',
                                '20:00',
                                '21:00'
                                ]
    });        
});
