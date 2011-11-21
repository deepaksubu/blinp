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
    print edge_array,len(edge_array)

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
    print len(entries), entries
if __name__=="__main__":
   edge_find("/home/deepak/Development/Processing/PLUM/DemoData1/PLUM-Bhubaneswar/Week_1/Device_29/data0000.csv")