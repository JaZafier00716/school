import xml.etree.ElementTree as ET

root = ET.parse('canteen.xml')

dates = root.findall('date')

for date in dates:
    print(date.attrib["day"])
    # meals = iter(date)
    # for meal in meals:
    for meal in date:
        print(f"\t{meal.attrib["name"]}")
        for ingredient in meal.iterfind('ingredient'):
            print(f"\t\t{ingredient.attrib['name']}")