<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Page Not Found</title>
    <link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/animate.css/4.1.1/animate.min.css"/>
    <style>
        @import url('https://fonts.googleapis.com/css2?family=Roboto:wght@400;700&display=swap');

        body {
            font-family: 'Roboto', sans-serif;
            background: url('https://media0.giphy.com/media/v1.Y2lkPTc5MGI3NjExcmRrYmQxMml4bW1uamZlZDQ0bHFjZmYzamdkMXg4cDh5cWpucHdwcCZlcD12MV9pbnRlcm5hbF9naWZfYnlfaWQmY3Q9Zw/lnaIzsFXSAss5osoy3/giphy.webp') no-repeat center center fixed;
            background-size: cover;
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
        }
        .container {
            text-align: center;
            background-color: rgba(255, 255, 255, 0.9);
            padding: 30px;
            border-radius: 8px;
            box-shadow: 0 0 20px rgba(0, 0, 0, 0.1);
            animation: fadeInDown 1s;
        }
        h1 {
            font-size: 3em;
            color: #ff5733;
            margin-bottom: 10px;
        }
        p {
            font-size: 1.2em;
            color: #333;
        }
        a {
            text-decoration: none;
            color: #17a2b8;
            font-weight: bold;
        }
        @keyframes fadeInDown {
            from {
                opacity: 0;
                transform: translate3d(0, -100%, 0);
            }
            to {
                opacity: 1;
                transform: none;
            }
        }
    </style>
</head>
<body>
<div class="container animate__animated animate__fadeInDown">
    <p>Page Not Found</p>
    <p><a href="${pageContext.request.contextPath}/">Go to Home</a></p>
</div>
</body>
</html>
