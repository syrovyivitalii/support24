<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Центр підтримки ЦОЗ, ЕК та ІТ</title>
    <style>
        body {
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background-color: #f7f9fc;
            color: #333;
            margin: 0;
            padding: 0;
            width: 100%;
        }
        .container {
            width: 100%;
            max-width: 600px;
            margin: 40px auto;
            background: #ffffff;
            border-radius: 12px;
            box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
            padding: 30px;
            box-sizing: border-box;
        }
        h1 {
            color: #1e90ff;
            font-size: 26px;
            margin-top: 0;
            text-align: center;
            font-weight: 600;
        }
        p {
            font-size: 16px;
            line-height: 1.6;
            margin: 0 0 15px;
        }
        .highlight {
            font-weight: 600;
            color: #1e90ff;
        }
        .footer {
            font-size: 14px;
            color: #777;
            text-align: center;
            margin-top: 30px;
            border-top: 1px solid #e0e0e0;
            padding-top: 20px;
        }
        .footer a {
            color: #1e90ff;
            text-decoration: none;
            font-weight: 600;
        }
        .logo {
            display: block;
            margin: 0 auto 20px;
            width: 120px;
        }
        .button-container {
            text-align: center;
            margin-top: 20px;
        }
        .button {
            display: inline-block;
            background-color: #72adfc;
            padding: 10px 20px;
            text-decoration: none;
            border-radius: 6px;
            font-weight: bold;
            font-size: 16px;
            text-align: center;
            margin-top: 20px;
        }
        .button:hover {
            background-color: #67bffd;
        }
    </style>
</head>
<body>
<div class="container">
    <h1>ГУ ДСНС України у Львівській області</h1>
    <p>Вам призначено нове завдання:</p>
    <p><span class="highlight">Відповідальний:</span> ${assignedBy! "Не отримано імені"}</p>
    <p><span class="highlight">Дедлайн:</span> ${dueDate! "Дедлайн не призначено"}</p>
    <p><span class="highlight">Заявник:</span> ${createdBy}</p>
    <p><span class="highlight">Підрозділ:</span> ${unit}</p>
    <p><span class="highlight">Тип проблеми:</span> ${typeProblem}</p>
    <p><span class="highlight">Опис:</span> ${taskDescription! "Додатковий опис відсутній"}</p>
    <p><span class="highlight">Пріоритетність:</span> ${priority! "Пріоритетність не визначено"}</p>
    <div class="button-container">
        <a href=${baseUrl} class="button">Виконати</a>
    </div>
    <div class="footer">
        <p>Цей лист автоматично згенерований системою підтримки.</p>
        <p>Якщо у вас є питання, будь ласка, <a href="mailto:dsns.sprt.24@gmail.com">зв'яжіться з нами</a>.</p>
    </div>
</div>
</body>
</html>
