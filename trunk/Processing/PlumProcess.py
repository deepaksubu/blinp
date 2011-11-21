from numpy import *

def truncate_time(input):
    return (((input/1000.0)/3600.0)/24.0)

def edge_find(file_path):
    device_log=open(file_path)
    li=device_log.read()
    init_time=734332.6396
    start_time=734334.6458
    end_time=734341.6014
    timeseries_array_str=li.split(",")[0:-1]
#    print timeseries_array_str

    #add the init time
    timeseries_array_full=[(truncate_time(float(ele))+init_time) for ele in timeseries_array_str][1:]
    
    timeseries_array=[]

#   truncate time series array between start and end time
    for ele in range(0,len(timeseries_array_full)):
        if ( timeseries_array_full[ele] >= start_time and timeseries_array_full[ele] <= end_time):
            timeseries_array.append(timeseries_array_full[ele])
    
    final_timestamp=timeseries_array[len(timeseries_array)-1]
    length=len(timeseries_array)

    temp_array=timeseries_array
    timeseries_array=[] 

    #delete the last hour of data
    for ele in temp_array: 
        if (final_timestamp-ele >= 1/24.0):        
            timeseries_array.append(ele)
        #else:
           # print ele
                      
    #print timeseries_array
    edge_detection_constant=truncate_time(15000.0)#0.000173611111111
    max_edge_length=truncate_time(120000.0)
    edge_array=[]
    end_array=[]
    count_array=[]
    tcounter=0
    #for tcounter in range(len(timeseries_array)):
    while tcounter<len(timeseries_array):
        t0=timeseries_array[tcounter] 
        edge_array.append(t0)
        count=1   
        if (tcounter==len(timeseries_array)-1):
                break  
        else:
                dist_btw_motion=timeseries_array[tcounter+1]-timeseries_array[tcounter]  
            
        dist_btw_motion= timeseries_array[tcounter+1]-timeseries_array[tcounter]
        while dist_btw_motion < edge_detection_constant and tcounter < len(timeseries_array):
            tcounter=tcounter+1
            count=count+1
            if (tcounter==len(timeseries_array)-1):
                break  
            else:
                dist_btw_motion=timeseries_array[tcounter+1]-timeseries_array[tcounter]  
            
        #declare new end 
        end_array.append(timeseries_array[tcounter])
        #append the count of the edges
        count_array.append(count)
         
        if ((timeseries_array[tcounter]-t0)>max_edge_length):
            end_array.append(count)
            edge_array.append(count)
            count_array.append(-1)

        if (count_array[len(count_array)-1]<2):
                count_array.pop()
                edge_array.pop()
                end_array.pop()
        tcounter=tcounter+1
       
       # if tcounter==(len(timeseries_array)-2):
       #     break;
        
    events_from_edges(edge_array,end_array)
#    print "end_array" , end_array
#    print "edge_array" , edge_array
#    print "count_array", count_array, len(count_array)

def events_from_edges(edge_array,end_array):
#    print edge_array,len(edge_array)

    edge_subsumption_threshold=0.006944444
    min_activity_length=0.00017361
    rejected=0
    entries=[]
    exits=[]
    ed_ctr=0
    count=0
      
    while ed_ctr < len(edge_array):
        temp_edge=edge_array[ed_ctr]
#        print temp_edge
    
# % subsume following edge based on time threshold
#            while m < length(edges{i,j}) && ...
#                    edges{i,j}(m+1)-edges{i,j}(m) < t1
#                count = count + 1;
#                m = m+1;
#            end
        dist_btw_events=edge_array[ed_ctr+1]-edge_array[ed_ctr]
        while dist_btw_events < edge_subsumption_threshold:
            ed_ctr=ed_ctr+1
            count=count+1
            if ed_ctr == len(edge_array)-1:
                break
            else:
                dist_btw_events=edge_array[ed_ctr+1]-edge_array[ed_ctr]

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
        if ed_ctr==len(edge_array):
           break
    actDur=findActDur(entries,exits)
    edgeData=findRelevantEdges(edge_array,end_array,entries , exits)
    t1=0.0041667
    t2=0.0010417
    splitActivity( entries , exits , edgeData , actDur , t1 , t2 )
#    print len(entries), entries
    

def findActDur(entries,exits):
    activity_dur = []
    for ele in range(0,len(entries)):
        activity_dur.append(exits[ele]-entries[ele])
    return activity_dur

def findRelevantEdges( edges , ends , entries , exits):
    edgeData =[None]*len(entries)

#The most fucked up logic yet
    for i in range(0,1):
#        edgeData.append([None]*2)#.append = cell(length(entries{i}),1);
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

#           edgeData[i][0]=templist
#edgeData[i].append([])
            end_list=where(logical_and(array(ends) >= entries[j],array(ends) <=exits[j])==True,1,0)
            templist=list(end_list)
            trunclist=[]
            for ele in range(0,len(templist)):
                if templist[ele] != 0.0:
                    trunclist.append(ends[ele])
            edgeData[j][1]=trunclist
    return edgeData

                    

#            edgeData[i].append([])
#            edgeData[i][1]=templist
            #print logical_list
#        edgeData[i][j][0] = edges[i];
#        edgeData[i][j][1] = ends{i}(ends{i} >= entries{i}(j) & ...
#            ends{i} <= exits{i}(j));
#    print edgeData

def splitActivity( entries , exits , edgeData , actDur , t1 , t2 ):
    newEntry = []#[None]*len(entries)#cell(size(entries));
    newExit =  []#[None]*len(entries)#cell(size(entries));

    loneEnd = []#[None]*len(entries)#cell(size(entries));

    for i in range(0,1):#for i = 1:length(entries)              % loop through each HH
        for j in range(0,len(entries)):#for j = 1:length(entries{i})       % loop through each activity start
            newEntry.append(entries[j])#newEntry{i} = [newEntry{i}; entries{i}(j)];   % add new entry
            loneEnd.append(-1)#loneEnd{i} = [loneEnd{i}; -1];
        
#        % check activity threshold
            if actDur[j] > t1:
                loneEnd[-1] = 0;
                tempEdges=edgeData[j][0]#tempEdges = edgeData{i}{j}{1};   % grab relevant edges / ends
                tempEnds=edgeData[j][1]#tempEnds = edgeData{i}{j}{2};
            
#            % loop through edges
                m = 1#            m = 2;
                while m<(len(tempEdges)-1):#while m < length(tempEdges)
                    if tempEdges[m+1] - tempEnds[m]>t2:#if tempEdges(m+1) - tempEnds(m) > t2
                   #                % if edge-to-end > t2
                        newExit.append(tempEnds[m])#newExit{i} = [newExit{i}; tempEnds(m)];
                        newEntry.append(tempEdges[m+1])#newEntry{i} = [newEntry{i}; tempEdges(m+1)];
                        loneEnd.append(0)#loneEnd{i} = [loneEnd{i}; 0];
                        print "Before m",m
                        if m+1 ==len(tempEdges):#if m+1 == length(tempEdges)
                            loneEnd.append(1)#loneEnd{i}(end) = 1;
                    #end
                        print "After m",m    
                        m = m+1;

                #end
                    m = m+1;
            #end
            
        #end
            newExit.append(exits[j])# newExit{i} = [newExit{i}; exits{i}(j)];
        print newExit,len(newExit)
        
#    end
    
#end

#end

#    end
                       
    
#end

#end


if __name__=="__main__":
   edge_find("/home/deepak/Development/Processing/PLUM/DemoData1/PLUM-Bhubaneswar/Week_1/Device_29/data0000.csv")
