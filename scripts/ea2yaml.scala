/**
  * Created by cmartin on 24/04/16.
  */

val xmlFilePath = "resources/ea-classes.xml"

// process node
/*
def processNode(node: Node): String =
  node match {
    case <xs:element{attributes}/> => "it's an element with attributes: " + attributes
    case _ => "Not recognized"
}
*/

// read xml file from disk
val xmlFile = loadFile(xmlFilePath)
//println(xmlFile)

val elements = xmlFile \ "complexType"
val elementsCount = elements.size
println(f"elements has $elementsCount elements")

for (element <- elements) {
  println(element \ "@name")
  val attributes = element \\ "element" \\ "@name"
  attributes.map(a => println(f" -$a"))
}