var Index = {};

(function() {
	
	Index.studentSelected = 0;
	Index.coursesTable = null;
	Index.selectedRow = null;
	Index.selectedCourseToAdd = 0;
	
	Index.initializer = function(){
		Index.loadStudents();
		$("#studentsSelect").change(function(){
			Index.studentSelected = this.value;
			Index.getStudentCourses();
		});
		$("#addCourse").click(Index.AddNewCourse);
		$("#delteCourse").click(Index.deleteActiveCourse);
		$("#changeScore").click(Index.changeScore);
		$("#saveScore").click(Index.saveScore);
		$("#saveCourse").click(Index.saveCourse);
		$("#notSaveScore").click(Index.cancelSaveScore);
		$("#notSaveCourse").click(Index.cancelSaveCourse);
	}
	
	Index.saveScore = function(){
		var newCourseScore = $("#newScore").val();
		if(newCourseScore >= 0 && newCourseScore <= 5){
			var url = "http://localhost:8080/sistemadenotas/Services"
			$.ajax({
	            type: "POST",
	            data: "function=change_score&studentId=" + Index.studentSelected 
	            	+ "&courseId=" + Index.selectedRow.code + "&newCourseScore=" + newCourseScore,
				url: url,
				success: function (result, handler) {
					if(result == "200"){
						alert("Ejecucion exitosa");
					}else{
						alert("Error en ejecucion");
					}
					Index.getStudentCourses();
					$(".ContentChangeScore").removeClass("ShowElement");
					$(".ContentChangeScore").addClass("HiddenElement");
				}
			});
		}else{
			alert("Ha ingresado una calificacion erronea, recuerde que debe estar entre 0 y 5.");
		}
	}
	
	Index.cancelSaveScore = function(){
		$(".ContentChangeScore").removeClass("ShowElement");
		$(".ContentChangeScore").addClass("HiddenElement");
		$("#newScore").val("");
	}
	
	Index.cancelSaveCourse = function(){
		$(".ContentCoursesSelect").removeClass("ShowElement");
		$(".ContentCoursesSelect").addClass("HiddenElement");
	}
	
	Index.saveCourse = function(){
		if(Index.studentSelected != 0 && Index.selectedCourseToAdd != 0){
			var url = "http://localhost:8080/sistemadenotas/Services"
			$.ajax({
	            type: "POST",
	            data: "function=add_course&studentId=" + Index.studentSelected 
	            	+ "&courseId=" + Index.selectedCourseToAdd,
				url: url,
				success: function (result, handler) {
					if(result == "200"){
						alert("Ejecucion exitosa");
					}else{
						alert("Error en ejecucion");
					}
					Index.getStudentCourses();
					$(".ContentCoursesSelect").removeClass("ShowElement");
					$(".ContentCoursesSelect").addClass("HiddenElement");
				}
			});
		}else{
			alert("Debe seleccionar un estudiante y un curso.");
		}
	}
	
	Index.changeScore = function(){
		if(Index.studentSelected != 0 && Index.selectedRow != null){
			$(".ContentChangeScore").removeClass("HiddenElement");
			$(".ContentChangeScore").addClass("ShowElement");
		}else{
			alert("Debe seleccionar un estudiante y un curso.");
		}
	}
	
	Index.AddNewCourse = function(){
		if(Index.studentSelected != 0){
			$(".ContentCoursesSelect").removeClass("HiddenElement");
			$(".ContentCoursesSelect").addClass("ShowElement");
			var url = "http://localhost:8080/sistemadenotas/Services?function=get_courses"
			$.ajax({
	            type: "GET",
				url: url,
				success: function (data, handler) {
					$("#coursesSelect").empty();
					$("#coursesSelect").append("<option value='0'>Seleccione un curso</option>");
					Index.selectedCourseToAdd = 0;
					for(var i=0 ; i<data.length; i++){
						var course = data[i];
						$("#coursesSelect").append("<option value=" + course.code + ">" 
								+ course.name + "</option>");
					}
					$("#coursesSelect").change(function(){
						Index.selectedCourseToAdd = this.value;
					})
				}
			});
		}else{
			alert("Debe seleccinar un estudinte")
		}
	}
	
	Index.deleteActiveCourse = function(){
		if(Index.studentSelected != 0 && Index.selectedRow != null){
			var url = "http://localhost:8080/sistemadenotas/Services"
			$.ajax({
	            type: "POST",
	            data: "function=delete_course&studentId=" + Index.studentSelected 
	        	+ "&courseId=" + Index.selectedRow.code,
				url: url,
				success: function (result, handler) {
					if(result == "200"){
						alert("Ejecucion exitosa");
					}else{
						alert("Error en ejecucion");
					}
					Index.getStudentCourses();
				}
			});
		}else{
			alert("Debe seleccionar un estudiante y un curso.");
		}
	}
	
	Index.loadStudents = function(){
		var url = "http://localhost:8080/sistemadenotas/Services?function=get_students"
		$.ajax({
            type: "GET",
			url: url,
			success: function (data, handler) {
				for(var i=0 ; i<data.length; i++){
					var student = data[i];
					$("#studentsSelect").append("<option value=" + student.identification + ">" 
							+ student.name + " " + student.lastName + "</option>");
				}
			}
		});
	}
	
	Index.getStudentCourses = function(studentId){
		if(Index.studentSelected != 0){
			var url = "http://localhost:8080/sistemadenotas/Services"
			$.ajax({
	            type: "POST",
	            data: "function=get_courses_by_student&studentId=" + Index.studentSelected,
				url: url,
				success: function (data, handler) {
					Index.configureCoursesTable(data);
				}
			});
		}else{
			alert("Debe seleccionar un estudiante");
		}
	}
	
	Index.configureCoursesTable = function(data){
		if(Index.coursesTable != null){
			Index.coursesTable.destroy();
			$("#coursesTable").empty();
			Index.coursesTable = null;
			Index.selectedRow = null;
		}
		Index.coursesTable = $("#coursesTable").DataTable({
			"columns": [
			            { "data": "code", "title": "Codigo"},
			            { "data": "name", "title": "Nombre"},
			            { "data": "score", "title": "Nota"},
			        ],
			"aaData":data
		});
		$('#coursesTable tbody').on( 'click', 'tr', function () {
	        if ( $(this).hasClass('selected') ) {
	            $(this).removeClass('selected');
	            Index.selectedRow = null;
	        }
	        else {
	        	Index.coursesTable.$('tr.selected').removeClass('selected');
	            $(this).addClass('selected');
	            Index.selectedRow = Index.coursesTable.row(this).data()
	        }
	    } );
	}
	
})();

$(function() {
	Index.initializer();
})