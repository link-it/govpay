<xsl:stylesheet version="1.0"
 xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
 xmlns:w="http://java.sun.com/xml/ns/javaee">
 <xsl:output omit-xml-declaration="yes" indent="yes"/>

 <xsl:template match="node()|@*">
  <xsl:copy>
   <xsl:apply-templates select="node()|@*"/>
  </xsl:copy>
 </xsl:template>
 <xsl:template match="//w:listener[w:listener-class='it.govpay.web.listener.HttpSessionCheckListener']"/>
</xsl:stylesheet>

