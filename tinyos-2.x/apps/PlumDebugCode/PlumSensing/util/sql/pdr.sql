
-- Compute the real Packet Deliver Ratio
SELECT max(ts) AS last_packet,
       origin, 
       count(DISTINCT udp_seqno) / (max(udp_seqno) - min(udp_seqno) + 1) AS pdr, 
       count(DISTINCT udp_seqno) AS rxed,
       (max(udp_seqno) - min(udp_seqno) + 1) AS total
FROM lpl_1 
GROUP BY origin;
