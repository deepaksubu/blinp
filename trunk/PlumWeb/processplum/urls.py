from django.conf.urls.defaults import patterns, include, url

# Uncomment the next two lines to enable the admin:
# from django.contrib import admin
# admin.autodiscover()

urlpatterns = patterns('processplum.views',
    # Examples:
    # url(r'^$', 'plumweb2.views.home', name='home'),
    # url(r'^plumweb2/', include('plumweb2.foo.urls')),
      url(r'^calculate/','index' ),
      url(r'^upload/','upload_file' ),
    # Uncomment the admin/doc line below to enable admin documentation:
    # url(r'^admin/doc/', include('django.contrib.admindocs.urls')),

    # Uncomment the next line to enable the admin:
    # url(r'^admin/', include(admin.site.urls)),
)
