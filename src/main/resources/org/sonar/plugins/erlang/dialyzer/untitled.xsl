<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform"
    version="1.0">
    <xsl:template match="/">
        <profile>
            <name>Default Erlang Profile</name>
            <language>erl</language>
            <rules>
                 <xsl:for-each select="//rule">
                     <rule>
                     <xsl:call-template name="rule"></xsl:call-template>
                     </rule>
                 </xsl:for-each>    
            </rules>            
        </profile>        
    </xsl:template>
    <xsl:template name="rule">
        <repositoryKey>Erlang</repositoryKey>
        <key><xsl:value-of select="./key"/></key>
        <priority>MAJOR</priority>        
    </xsl:template>
</xsl:stylesheet>