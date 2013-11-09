<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>api login test</title>
</head>
<body>
    <form:form method="post" action="login">
        <form:input appointmentType="text" path="username"/>
        <form:input appointmentType="password" path="password"/>
        <button type="submit">Submit</button>
    </form:form>
</body>
</html>