<!DOCTYPE html>
<html lang="en">
  <head>
    <meta charset="utf-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->
    <meta name="description" content="">
    <meta name="author" content="">

    <title>yaohw微服务统一认证</title>

    <link href="/css/bootstrap.min.css" rel="stylesheet">
    <link href="/css/signin.css" rel="stylesheet">
  </head>

  <body>
    <div class="container form-margin-top">
      <!-- 这里请求方法为post，配置的url跟formLogin().loginPage("/login")的login一致，如果配formLogin().loginPage("/login").loginProcessingUrl("/form") 那么配置form -->
      <form class="form-signin" action="/login" method="POST">
        <h2 class="form-signin-heading" align="center">统一认证系统</h2>
        <input type="text" name="username" class="form-control form-margin-top" placeholder="账号" required autofocus>
        <input type="password" name="password" class="form-control" placeholder="密码" required>
        <button class="btn btn-lg btn-primary btn-block" type="submit">登 录</button>
      </form>
    </div>
    <footer>
      <p>support by: yaohw</p>
      <p>email: <a href="mailto:yaohw484@gmail.com">yaohw484@gmail.com</a>.</p>
    </footer>
  </body>
</html>
