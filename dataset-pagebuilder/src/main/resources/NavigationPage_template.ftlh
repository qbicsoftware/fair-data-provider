<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Dataset Navigation page</title>
</head>
<body>
<h1>All data catalogs and datasets available</h1>

<p>Dataset name: ${name}</p>
<p>Description: ${description}</p>
<p>License to use this dataset: ${license}</p>
<p>${url}</p>
<p>${@id}</p>
<p>${identifier}</p>
<p>${@type}</p>
<ul>some keywords:
    <#list keywords as key,value>
        <li>${key} :  ${value}</li>
    </#list>
</ul>
<script>
<--- bioschemas markup --->
    ${markup}
</script>
</body>
</html>

