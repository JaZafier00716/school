
class Document:
  no_of_documents = 0
  total_length = 0

  def __init__(self, content) -> None:
    self._content = content
    Document.no_of_documents += 1
    Document.total_length += len(content)

  @property # Technically a Getter, but in Python we use it as a property
  def content(self):
    return self._content

  @content.setter # Technically a Setter, but in Python we use it as a property
  def content(self, value):
    Document.total_length -= len(self._content)
    self._content = value
    Document.total_length += len(value)

  @staticmethod
  def average_length():
    return Document.total_length / Document.no_of_documents
  
  def __str__(self) -> str:
    return f"Document: {self.content[:10]}{ '...' if len(self.content) > 10 else ''}"
  
  def __repr__(self) -> str:
    return f"Document: {self.content[:10]}{ '...' if len(self.content) > 10 else ''}"

class SearchEngine:
  def __init__(self):
    self.documents: list[Document] = []

  def add_document(self, document: Document):
    self.documents.append(document)

  def __iter__(self):
    for doc in self:
      yield doc

  def search(self, **query):
    documents: list[Document] = []
    for doc in self.documents:
      if 'contains' in query:
        if doc.content.find(query['contains']) >= 0:
          documents.append(doc)
        if 'icontains' in query:
          if doc.content.lower().find(query['icontains'].lower()) >= 0:
            yield doc
    #return documents


d1 = Document("Text")
print(d1.content)
print(Document.no_of_documents)
print(Document.average_length())


d2 = Document("Another text")
print(d2.content)
print(Document.no_of_documents)
print(Document.average_length())

d2.content = "Another text with more words"
print(Document.average_length())

print(d2)


se = SearchEngine()
se.add_document(d1)
se.add_document(d2)

print("Using iterator:")
for doc in se:
  print(doc)

print("Case sensitive:", list(se.search(contains="text")))
print("Case insensitive:", list(se.search(icontains="text")))

