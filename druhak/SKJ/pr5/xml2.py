import xml.etree.ElementTree as ET

def xml2py(node):
    name = node.tag

    pytype = type(name, (object,), {})
    pyobj = pytype()

    for attr in node.attrib.keys():
        setattr(pyobj, attr, node.get(attr))

    if node.text and node.text != '' and node.text != " " and node.text != "\n":
        setattr(pyobj, "text", node.text)

    for cn in node:
        if not hasattr(pyobj, cn.tag):
            setattr(pyobj, cn.tag, [])
        getattr(pyobj, cn.tag).append(xml2py(cn))

    return pyobj



with open("canteen.xml", "r") as file:
    xml_data = file.read()

    canteen_xml_tree = ET.fromstring(xml_data)

    obj = xml2py(canteen_xml_tree)

    for date in obj.date:
        print(date.day)
        for meal in date.meal:
            print(f"\t{meal.name}")
            for ingredient in meal.ingredient:
                print(f"\t\t{ingredient.name}")