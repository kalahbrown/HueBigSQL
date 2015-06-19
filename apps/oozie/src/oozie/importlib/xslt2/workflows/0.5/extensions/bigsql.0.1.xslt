<?xml version="1.0"?>

<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" xmlns:workflow="uri:oozie:workflow:0.5" xmlns:bigsql="uri:oozie:bigsql-action:0.1" exclude-result-prefixes="workflow bigsql">

<xsl:import href="../nodes/fields/script_path.xslt"/>

<xsl:template match="bigsql:bigsql">

  ,"bigsql": {<xsl:call-template name="script_path"/>}

</xsl:template>

</xsl:stylesheet>
