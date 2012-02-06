from numpy import *
from time import localtime, strftime

def truncate_time(input):
    return (((input/3600.0000)/24.000))

def epochtime(input):
    return (((input*3600.0000)*24.000))

def edge_find(file_path):
#    file1=open("/home/deepak/Dropbox/PlumJar/PlumData250_1325134710484.csv")
    file1=open(file_path)
    string1=file1.read();
    li1=string1.split('\n');
    li=[ele.split(',')[2] for ele in li1[2:]]
    

#    device_log=open(file_path)
#    li=device_log.read()
#    init_time =734332.6319444445
#    start_time=734334.6527777778
#    end_time=734341.6111111111
    timeseries_array_str=li#.split(",")[0:-1]
    #add the init time
    timeseries_array_full=[(truncate_time(float(ele))) for ele in timeseries_array_str][0:]
    print timeseries_array_full[1:10],"normal"
    timeseries_array_full.sort();
    print timeseries_array_full[1:10],"sorted"
    timeseries_array=[]
#   truncate time series array between start and end time
    for ele in range(0,len(timeseries_array_full)):
       # if ( timeseries_array_full[ele] >= start_time and timeseries_array_full[ele] <= end_time):
            timeseries_array.append(timeseries_array_full[ele])

    final_timestamp=timeseries_array[len(timeseries_array)-1]

    temp_array=timeseries_array
    timeseries_array=[] 
    #delete the last hour of data
    for ele in temp_array: 
        if (final_timestamp-ele >= 1/24.0):        
            timeseries_array.append(ele)

    edge_detection_constant=truncate_time(15.00000)#0.000173611111111
    max_edge_length=truncate_time(120.00000)
    edges=[]
    ends=[]
    counts=[]
    tcounter=1
    while tcounter<len(timeseries_array):
                ##Everything is same till here  more or less there is some issue at the end of the decimal point          
        t0=timeseries_array[tcounter] 
        edges.append(t0)
        count=1   
        try:
            while timeseries_array[tcounter+1]-timeseries_array[tcounter] < edge_detection_constant and tcounter < (len(timeseries_array)-1):
                tcounter=tcounter+1
                count=count+1
        except IndexError:
            print "IndexOutOfRange"

            
        #declare new end 
        ends.append(timeseries_array[tcounter])
        #append the count of the edges
        counts.append(count)
         
        if ((timeseries_array[tcounter]-t0)>max_edge_length):
            ends.append(count)
            edges.append(count)
            counts.append(-1)

        if (counts[len(counts)-1]<2):
                counts.pop()
                edges.pop()
                ends.pop()
        tcounter=tcounter+1
#    print edges,len(edges)
    dictEnEx=events_from_edges(edges,ends)
    return dictEnEx

def events_from_edges(edge_array,end_array):
    edge_subsumption_threshold=0.006944444444
    min_activity_length       =0.000173611111
    rejected=0
    entries=[]
    exits=[]
    ed_ctr=0
    count=0
      
    while ed_ctr < len(edge_array):
        temp_edge=edge_array[ed_ctr]
        try:
            while edge_array[ed_ctr+1]-edge_array[ed_ctr]< edge_subsumption_threshold and ed_ctr < len(edge_array)-1:
                ed_ctr=ed_ctr+1
                count=count+1
        except IndexError:
            print "IndexHappilyOutOfRangeNeedToPutSomeNiceLogicHere"
    
        temp_end=end_array[ed_ctr]
        
        
#        print temp_end - temp_edge, ed_ctr
        if count == 0 and temp_end-temp_edge < min_activity_length:
             #   %disp(datestr(tempEdge))         % display rejected time
                rejected = rejected+1          # increment rejected count
                ed_ctr = ed_ctr+1
                
#            % else declare entry, exit
        else:
                entries.append(temp_edge)
                exits.append(temp_end)
                ed_ctr = ed_ctr+1
                count = 0

 #   print entries, len(entries)
 #   print rejected
    actDur=findActDur(entries,exits)
    edgeData=findRelevantEdges(edge_array,end_array,entries , exits)
    t1=0.0041667
    t2=0.0010417
    dictEntryExit=splitActivity( entries , exits , edgeData , actDur , t1 , t2 )
    return dictEntryExit
#    print len(entries), entries
    

def findActDur(entries,exits):
    activity_dur = []
    for ele in range(0,len(entries)):
        activity_dur.append(exits[ele]-entries[ele])
#    print activity_dur, len(activity_dur)
    return activity_dur

def findRelevantEdges( edges , ends , entries , exits):
    edgeData =[None]*len(entries)
    for j in range(0,len(entries)):
        edgeData[j]=[None]*2
            #edgeData[i].append([])
        edge_list=where(logical_and(array(edges) >= entries[j],array(edges) <=exits[j])==True,1,0)
        templist=list(edge_list)
        trunclist=[]
        for ele in range(0,len(templist)):
            if templist[ele] != 0.0:
                trunclist.append(edges[ele])
        edgeData[j][0]=trunclist
        end_list=where(logical_and(array(ends) >= entries[j],array(ends) <=exits[j])==True,1,0)
        templist=list(end_list)
        trunclist=[]
        for ele in range(0,len(templist)):
            if templist[ele] != 0.0:
                trunclist.append(ends[ele])
        edgeData[j][1]=trunclist
    return edgeData

def splitActivity( entries , exits , edgeData , actDur , t1 , t2 ):
    newEntry = []
    newExit =  []

    loneEnd = []

    for i in range(0,1):
        for j in range(0,len(entries)):
            newEntry.append(entries[j])
            loneEnd.append(-1)
            if actDur[j] > t1:
                loneEnd[-1] = 0;
                tempEdges=edgeData[j][0]
                tempEnds=edgeData[j][1]
                m = 1
                while m<(len(tempEdges)-1):
                    if tempEdges[m+1] - tempEnds[m]>t2:
                        newExit.append(tempEnds[m])
                        newEntry.append(tempEdges[m+1])
                        loneEnd.append(0)
                        if m+1 ==len(tempEdges):
                            loneEnd.append(1)
                        m = m+1;
                    m = m+1;
            newExit.append(exits[j])
    print newExit,len(newExit)
    print newEntry,len(newEntry)
    newEntry=[strftime("%a, %d %b %Y %H:%M:%S +0000", localtime(epochtime(element))) for element in newEntry ]
    newExit=[strftime("%a, %d %b %Y %H:%M:%S +0000", localtime(epochtime(element))) for element in newExit ]
    return {"newEntry":newEntry,"newExit":newExit}

if __name__=="__main__":
    edge_find("/home/deepak/development/processing/PLUM/DemoData1/PLUM-Bhubaneswar/Week_1/Device_14/data0000.csv")
