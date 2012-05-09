<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
    <xsl:template match="/">
        <rules>
            <xsl:for-each select="//rule">
                <xsl:element name="rule">
                    <xsl:attribute name="key">
                        <xsl:value-of select="./key"/>
                    </xsl:attribute>
                    <xsl:call-template name="rule"/>
                </xsl:element>
            </xsl:for-each>
        </rules>
    </xsl:template>
    <xsl:template name="rule">
        <name>
            <xsl:value-of select="./name"/>
        </name>
        <configKey>
            <xsl:value-of select="./configKey"/>
        </configKey>
        <description>
            <xsl:value-of select="./description"/>
        </description>
        <messages>
            <xsl:for-each select="./messages/message">
                <message>
                    <xsl:value-of select="."/>
                </message>
            </xsl:for-each>
        </messages>
    </xsl:template>
</xsl:stylesheet>