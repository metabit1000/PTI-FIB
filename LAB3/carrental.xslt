<?xml version="1.0" encoding="UTF-8"?>
<carrental xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance" xsi:noNamespaceSchemaLocation="carrental.xsd">
</carrental>
<xsl:stylesheet version="1.0" xmlns:xsl="http://www.w3.org/1999/XSL/Transform" >
  <xsl:template match="/carrental">
      <html>
          <head><title>RENTALS</title></head>
          <body>
              <xsl:apply-templates select="rental"/>
          </body>
      </html>
  </xsl:template>
  <xsl:template match="rental">
      <table border="0">
          <h1>MAKE=<xsl:value-of select="make"/></h1><br/>
          <h1>MODEL=<xsl:value-of select="model"/></h1><br/>
          <h1>NOFDAY=<xsl:value-of select="nofday"/></h1><br/>
          <h1>NOFUNITS=<xsl:value-of select="nofunits"/></h1><br/>
          <h1>DISCOUNT=<xsl:value-of select="discount"/></h1><br/>
      </table>
  </xsl:template>
</xsl:stylesheet>
