$(function () {
    // VARIABLES =============================================================
    var ACCESS_TOKEN_KEY = "oAuth2AccessToken"
    var REFRESH_TOKEN_KEY = "oAuth2RefreshToken"
    var $notLoggedIn = $("#notLoggedIn");
    var $loggedIn = $("#loggedIn").hide();
    var $responseSpl1 = $("#responseSpl1");
    var $responseSpl2 = $("#responseSpl2");
    var $login = $("#login");
    var $userInfo = $("#userInfo").hide();

    // FUNCTIONS =============================================================
    function getOAuth2AccessToken() {
        return sessionStorage.getItem(ACCESS_TOKEN_KEY);
    }
    
    function getOAuth2RefreshToken() {
        return sessionStorage.getItem(REFRESH_TOKEN_KEY);
    }

    function setOAuth2AccessToken(token) {
    	sessionStorage.setItem(ACCESS_TOKEN_KEY, token);
    }
    
    function setOAuth2RefreshToken(token) {
    	sessionStorage.setItem(REFRESH_TOKEN_KEY, token);
    }

    function removeOAuth2AccessToken() {
    	sessionStorage.removeItem(ACCESS_TOKEN_KEY);
    }

    function removeOAuth2RefreshToken() {
    	sessionStorage.removeItem(REFRESH_TOKEN_KEY);
    }
    
    function doLogin(loginData) {
        $.ajax({
            url: "/proxy/login",
            type: "POST",
            data: JSON.stringify(loginData),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (data, textStatus, jqXHR) {
                setOAuth2AccessToken(data.accessToken);
                setOAuth2RefreshToken(data.refreshToken);
                $login.hide();
                $notLoggedIn.hide();
                showTokenInformation()
                showUserInformation();
            },
            error: function (jqXHR, textStatus, errorThrown) {
                if (jqXHR.status === 401) {
                    $('#loginErrorModal')
                        .modal("show")
                        .find(".modal-body")
                        .empty()
                        .html("<p>Spring exception:<br>" + jqXHR.responseJSON.exception + "</p>");
                } else {
                    throw new Error("an unexpected error occured: " + errorThrown);
                }
            }
        });
    }

    function doLogout() {
        removeOAuth2AccessToken();
        removeOAuth2RefreshToken();
        $login.show();
        $userInfo
            .hide()
            .find("#userInfoBody").empty();
        $loggedIn
            .hide()
            .attr("title", "")
            .empty();
        $notLoggedIn.show();
    }

    function createAuthorizationTokenHeader() {
        var token = getOAuth2AccessToken();
        if (token) {
            return {"Authorization": token};
        } else {
            return {};
        }
    }

    function showUserInformation() {
        $.ajax({
            url: "/proxy/user",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                var $userInfoBody = $userInfo.find("#userInfoBody");

                $userInfoBody.append($("<div>").text("Username: " + data.username));
                $userInfoBody.append($("<div>").text("Email: " + data.email));

                var $authorityList = $("<ul>");
                data.authorities.forEach(function (authorityItem) {
                    $authorityList.append($("<li>").text(authorityItem.authority));
                });
                var $authorities = $("<div>").text("Authorities:");
                $authorities.append($authorityList);

                $userInfoBody.append($authorities);
                $userInfo.show();
            }
        });
    }

    function showTokenInformation() {
        $loggedIn
            .text("Token: " + getOAuth2AccessToken())
            .attr("title", "Token: " + getOAuth2AccessToken())
            .show();
    }

    function showResponseSpl1(statusCode, message) {
        $responseSpl1
            .empty()
            .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }

    function showResponseSpl2(statusCode, message) {
        $responseSpl2
            .empty()
            .text("status code: " + statusCode + "\n-------------------------\n" + message);
    }
    
    // REGISTER EVENT LISTENERS =============================================================
    $("#loginForm").submit(function (event) {
        event.preventDefault();

        var $form = $(this);
        var formData = {
            username: $form.find('input[name="username"]').val(),
            password: $form.find('input[name="password"]').val()
        };

        doLogin(formData);
    });

    $("#logoutButton").click(doLogout);

    $("#exampleServiceBtnSpl1").click(function () {
        $.ajax({
            url: "/proxy/persons-spl-1",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponseSpl1(jqXHR.status, JSON.stringify(data));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponseSpl1(jqXHR.status, errorThrown);
            }
        });
    });

    $("#adminServiceBtnSpl1").click(function () {
        $.ajax({
            url: "/proxy/protected-spl-1",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponseSpl1(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponseSpl1(jqXHR.status, errorThrown);
            }
        });
    });

    $("#exampleServiceBtnSpl2").click(function () {
        $.ajax({
            url: "/proxy/persons-spl-2",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponseSpl2(jqXHR.status, JSON.stringify(data));
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponseSpl2(jqXHR.status, errorThrown);
            }
        });
    });

    $("#adminServiceBtnSpl2").click(function () {
        $.ajax({
            url: "/proxy/protected-spl-2",
            type: "GET",
            contentType: "application/json; charset=utf-8",
            headers: createAuthorizationTokenHeader(),
            success: function (data, textStatus, jqXHR) {
                showResponseSpl2(jqXHR.status, data);
            },
            error: function (jqXHR, textStatus, errorThrown) {
                showResponseSpl2(jqXHR.status, errorThrown);
            }
        });
    });
    
    $loggedIn.click(function () {
        $loggedIn
                .toggleClass("text-hidden")
                .toggleClass("text-shown");
    });

    // INITIAL CALLS =============================================================
    if (getOAuth2Token()) {
        $login.hide();
        $notLoggedIn.hide();
        showTokenInformation();
        showUserInformation();
    }
});