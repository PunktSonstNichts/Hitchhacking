var app = {
  BASE_URL: "http://10.232.29.59/HitchHacking/Hitchhacking/server/",
  lat: null,
  lon: null,
  init: function(){
    app.geo();
    //fancy way of user input
    $(document).on( "focus", "input[data-advancedinput]", function(e){

      var placeholder = $(this).attr("placeholder");
      var name = $(this).attr("name");

      var description = $(this).attr("data-advancedinput");

      var input = $(this);

      console.log("advancedinput for " + name);

      $("#advancedinput_inputname").html(placeholder ? placeholder : name);
      $("#advancedinput_inputdescription").html(description);

      $("#advancedinput").slideDown();
      $("#advancedinput_input").val(input.val()).focus();
      $(document).on("click", "#advancedinput_back", function(){
        $("#advancedinput").slideUp();
      });
      $(document).on("keyup", "#advancedinput_input", function(e){
        if(e.keyCode == 13 || e.keyCode == 27){
            $("#advancedinput").slideUp();
        }else{
          input.val($(this).val());
        }
      });
    }).on("click", "button#login", function(){
      app.changeView("login");
    }).on('submit', 'form#loginform', function(e) {
			e.preventDefault();
			app.ajax(app.BASE_URL + "accessUsers.php", {email: $(this).children("*[name=email]").val(), password: $(this).children("*[name=password]").val(), f: "loginUser"}, (function(response){

				response = response[0];
				console.log(response.api);
				if(response.api){
					localStorage.setItem("api_token", response.api);
					localStorage.setItem("user", response.first_name);
          $("*[data-content='username']").text(response.first_name);
          app.changeView("DriverOrHiker");
				}else{
					alert("Wrong credentials");
				}
			}));
		}).on("click", "#driver_btn", function(){
      app.changeView("driver");
      var i = 1;

      function getHikes () {
         setTimeout(function () {
          	app.ajax(app.BASE_URL + "accessHikes.php", {api: localStorage.getItem("api_token"), driver_lat: app.lat, driver_lon: app.lon, f: "getHikerRequests"}, function(response){
              if(response.length >= 1){
                $("#res_set").html("");
              }else{
                localStorage.removeItem("curr_hikerequest");
              }
              for (var i = 0; i < response.length; i++){
                  var point = response[i];
                  $("#res_set").append("<div id='" + point.hike_id + "' class='point' style='margin-top: " + point.current_lon + "vh; margin-left: " + point.current_lat + "vw;''></div>");
                  localStorage.setItem("curr_hikerequest", JSON.stringify(point));
              }
              getHikes();
            });
         }, 5000)
      }

      getHikes();

    }).on("click", "#driverView", function() {
      hike = localStorage.getItem("curr_hikerequest");
      if(hike != null){
        hike_obj = JSON.parse(hike);
        console.log(hike_obj);
        $(body).append("<div id='modal'></div>");
      }
    }).on("click", "#driver_point", function(){
      app.changeView("DriverOrHiker");
    });



    //important stuff happens here:
    app.changeView("auth");
  },
  changeView: function(view){
    $("*[data-view][data-view!='" + view  + "']").hide();
    $("*[data-view='" + view  + "']").show();
  },
  ajax: function(url, data, response){
			//needs to be there on every call
			data.api_token = localStorage.getItem("api_token");

			$.ajaxSetup({
				beforeSend: function(xhr) {
			        xhr.withCredentials = true;
			        xhr.setRequestHeader('Accept', 'application/json');
			    }
			});
			console.log("Hitchhacking communicates :o");
			$.ajax({
				url: url,
				data: data,
				type: "POST",
				dataType: "json",
				error: function(data) {
								response({success: false, msg: "Sorry, something went wrong. Seems like there is an http error somewhere."});
				},
				success: function(data, textStatus, jqXHR) {
					//data = data.responseText;
					console.log(data);
          if (data == null){
            response({success: false, msg: "No data sent"});
          }else{
  					data.success = true;
  					response(data);
          }
				}
			});
		},
    geo: function(){
      if (navigator.geolocation) {
        (function rungeo() {
            navigator.geolocation.getCurrentPosition(updatePosition);

            setTimeout(rungeo, 900000);
        })();
        } else {
          // ERROR CLASS
        }

        function updatePosition(positon){
          app.lat = positon.coords.latitude;
          app.lon = positon.coords.longitude;
        }
    }
  }
