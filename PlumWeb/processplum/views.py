# Create your views here.
from django.http import HttpResponse,HttpResponseRedirect
from processplum.forms import UploadFileForm
from django.shortcuts import render_to_response
from django.core.context_processors import csrf
from django.views.decorators.csrf import csrf_protect,csrf_exempt
from PlumProcess import edge_find

@csrf_exempt
def index(request):
 #   print "Is this good or bad"
 #   print request.FILES.keys()#'/home/deepak/development/processing/plumweb2/processplum/data0000.csv')
#    datastr="hai.read()"
#    file_handle=open('name.txt')
    dictEdgeEntry=edge_find('name.txt')
    entries=[str(element) for element in dictEdgeEntry['newEntry']]
    exits=[str(element) for element in dictEdgeEntry['newExit']]
    return render_to_response('display.html',{'entries':entries,'exits':exits})

"""
def index(request):
    if request.method == 'POST': # If the form has been submitted...
        form = ContactForm(request.POST) # A form bound to the POST data
        if form.is_valid(): # All validation rules pass
            # Process the data in form.cleaned_data
            # ...
            return HttpResponseRedirect('/thanks/') # Redirect after POST
    else:
        form = ContactForm() # An unbound form

    return render_to_response('contact.html', {
        'form': form,
    })
"""

@csrf_exempt
def upload_file(request):
    print 'I am indeed here'

    if request.method == 'POST':
        form = UploadFileForm(request.POST, request.FILES)
        if form.is_valid():
            handle_uploaded_file(request.FILES['file'])
            return HttpResponseRedirect('/process/calculate/')
    else:
        form = UploadFileForm()
    return render_to_response('upload.html', {'form': form})

def handle_uploaded_file(f):
    destination = open('name.txt', 'wb+')
    #print "i isas"
    complete="";
    for chunk in f.chunks():
        destination.write(chunk)
#    print complete 
    #dictEdgeExit=edge_find(complete)
    destination.close()
    

