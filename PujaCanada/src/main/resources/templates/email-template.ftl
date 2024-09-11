<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <title>Thank You for Your Purchase</title>
    <style>
        body {
            font-family: Arial, sans-serif;
            background-color: #fff4e6;
            margin: 0;
            padding: 0;
            color: #333333;
        }
        .container {
            width: 80%;
            margin: 0 auto;
            background-color: #fff;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
        }
        .header {
            background-color: #ff7f00;
            color: white;
            padding: 10px;
            text-align: center;
            border-radius: 10px 10px 0 0;
        }
        .header h1 {
            margin: 0;
        }
        .header p {
            margin: 5px 0;
        }
        .content {
            background-color: #fafaf0;
            padding: 20px;
            border-radius: 5px;
        }
        .content h2 {
            color: #003366;
        }
        .content p {
            line-height: 1.6;
        }
        .table-container {
            margin-top: 20px;
        }
        table {
            width: 100%;
            border-collapse: collapse;
            margin-bottom: 20px;
        }
        table, th, td {
            border: 1px solid #ddd;
        }
        th, td {
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #f2f2f2;
        }
        .footer {
            background-color: #ff7f00;
            color: white;
            text-align: center;
            padding: 10px;
            font-size: 12px;
            border-radius: 0 0 10px 10px;
        }
        .footer p {
            margin: 5px 0;
        }
        .footer a {
            color: white;
            text-decoration: none;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>Puja Canada Services</h1>
            <p>Email: pujacanada@example.com | Address: 187 Street, City, Country | Cell: (123) 456-7890</p>
        </div>
        <div class="content">
            <p>Dear ${username},</p>
            <p>Thank you for choosing our service for your puja needs in Canada. We are dedicated to providing a seamless and spiritual experience at your doorstep.</p>
            <p>Your order and appointment details are as follows:</p>

            <div class="table-container">
                <h2>Ordered Products</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Item</th>
                            <th>Quantity</th>
                            <th>Total Price</th>
                            <th>Discount</th>
                            <th>Final Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list products as cartitem>
                        <tr>
                            <td>${cartitem.product.name}</td>
                            <td>${cartitem.quantity}</td>
                            <td>${cartitem.product.price}</td>
                            <td>${cartitem.product.discount}%</td>
                            <td>${cartitem.total}</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>

            <div class="table-container">
                <h2>Appointment Details</h2>
                <table>
                    <thead>
                        <tr>
                            <th>Puja Name</th>
                            <th>Time</th>
                            <th>Date</th>
                            <th>Puja Priest</th>
                            <th>Completed Status</th>
                           	<th>Total Price</th>
                            <th>Discount</th>
                            <th>Final Price</th>
                        </tr>
                    </thead>
                    <tbody>
                        <#list appointments as appointment>
                        <tr>
                            <td>${appointment.puja.name}</td>
                            <td>${appointment.appointmentTime}</td>
                            <td>${appointment.appointmentDate}</td>
                           <td>
					    <#if appointment.pujaPriest??>
					        ${appointment.pujaPriest.user.userName}
					    <#else>
					        Not Assigned
					    </#if>
						</td>
                           
                            <td>${appointment.completedStatus?string("Completed", "Pending")}</td>
                            <td>${appointment.puja.price}</td>
                            <td>${appointment.puja.discount}</td>
                            <td>${appointment.pujaFee}</td>
                        </tr>
                        </#list>
                    </tbody>
                </table>
            </div>

            <p><strong>Total Amount to Pay: $${totalAmount}</strong></p>

            <p>We have attached the invoice for your records. If you have any questions, please feel free to contact us.</p>
        </div>
        <div class="footer">
            <p><strong>Thank you for your purchase!</strong></p>
            <p>Address: 123 Street, City, Country</p>
            <p>
                <a href="https://facebook.com/yourcompany" target="_blank">Facebook</a> |
                <a href="https://twitter.com/yourcompany" target="_blank">Twitter</a> |
                <a href="https://instagram.com/yourcompany" target="_blank">Instagram</a>
            </p>
        </div>
    </div>
</body>
</html>
