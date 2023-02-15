<#ftl output_format="HTML">
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <#if name??><title>QBiC dataset ${name}</title></#if>
    <meta name="description" content="This is a dataset landing page for the test dataset ${name}">
</head>
<body>
<h1>Landing page</h1>

<#if name??><p>Dataset name: ${name}</p></#if>
<#if description??><p>Description: ${description}</p></#if>
<#if license??><p>License to use this dataset: ${license}</p></#if>
<#if url??><p>url: ${url}</p></#if>
<#if @id??><p>id: ${@id}</p></#if>
<#if identifier??><p>identifier: ${identifier}</p></#if>
<#if @type??><p>type: ${@type}</p></#if>
<#if keywords??><p>some keywords: ${"keywords"}</p></#if>

<script type="application/ld+json">
    <#outputformat "JSON">
        ${markup}
    </#outputformat>
</script>
</body>
</html>

