$('#editM').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var modId = 0; var modName = ""; var modCredits = 0;

    $(this).find('.modal-title').text('Add New Module');

    if(button.attr('id') !== "addModuleBtn") {
        modId = button.data('module_id');
        modName = button.data('module_name');
        modCredits = button.data('module_credits');
        $(this).find('.modal-title').text('Editing Module: ' + modName);
    }
    $('#module-id').val(modId);
    $('#module-name').val(modName);
    var modCred = $('#module-credits');
    modCred.val(modCredits);
});

$('#editG').on('show.bs.modal', function (event) {
    var button = $(event.relatedTarget);
    var id = 0; var assName = ""; var grade = 0; var maxGrade = 0; var weight = 0;

    var parentId = button.data('parent_id');
    var remaining = button.data('remaining');
    $(this).find('.modal-title').text('Add New Assignment');

    if(button.attr('id') !== "addGradeBtn") {
        id = button.data('grade_id');
        assName = button.data('ass_name');
        grade = button.data('ass_grade');
        maxGrade = button.data('ass_max');
        weight = button.data('ass_weight');
        remaining += weight;
        if(remaining > 100) remaining = 100;
        $(this).find('.modal-title').text('Editing Assignment: ' + assName);
    }

    $('#grade-id').val(id);
    $('#grade-parent-id').val(parentId);
    $('#ass-name').val(assName);
    $('#ass-grade').val(grade);
    $('#ass-max').val(maxGrade);
    var assWeight = $('#ass-weight');
    assWeight.val(weight);
    assWeight.prop('max', remaining);
});

//limit grade to less than maximum for that assignment
$('#ass-max').on('change', function(){
    var newMax = $('#ass-max').val();
    $('#ass-grade').prop('max', newMax);
});