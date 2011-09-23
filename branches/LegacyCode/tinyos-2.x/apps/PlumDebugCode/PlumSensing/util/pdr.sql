SELECT hex(origin), count(DISTINCT seqno), max(seqno) - min(seqno) + 1 FROM vlsb_1 where ts > '2009-11-26 00:00:00' GROUP BY origin 



