#!/usr/bin/env kscript

@file:MavenRepository("imagej-releases", "http://maven.imagej.net/content/groups/public")
@file:DependsOnMaven("de.mpicbg.scicmop.kip:kip:0.1-SNAPSHOT")


import de.mpicbg.scicomp.kip.bubbles
import de.mpicbg.scicomp.kip.show


val image = bubbles()


image.show()
