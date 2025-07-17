package jayho.common.snowflake;

import java.util.random.RandomGenerator;


public class Snowflake {

    private static final int UNUSED_BITS = 1;

    private static final int EPOCH_BITS = 41;

    private static final int NODE_ID_BITS = 10;

    private static final int SEQUENCE_BITS = 12;

    private static final long maxNodeId = (1L<<NODE_ID_BITS) -1;

    private static final long maxSequence = (1L<<SEQUENCE_BITS) - 1;

    private final long nodeId = maxNodeId; // 임시

    private final long epoch = 1752097800000L; // 2025.07.09

    private long lastTimeMillis = epoch;

    private long sequence = 0L;

//    public Snowflake(long nodeId) {
//        this.nodeId = nodeId;
//    }

    public synchronized long nextId(){

        long currentTimeMillis = System.currentTimeMillis();

        // 현재 시간 < 마지막 시간 => Invalid
        if (currentTimeMillis < lastTimeMillis){
            System.out.println(currentTimeMillis + " " + lastTimeMillis);
            throw new IllegalStateException("Invalid Time");
        }

        // 시간이 같을 경우 => 서로 겹치지 않게 1씩 증가시킴
        if (currentTimeMillis == lastTimeMillis){
            sequence = (sequence + 1) & maxSequence;
            if (sequence == 0){ // 최대값 이상일경우
                currentTimeMillis = waitNextMills(currentTimeMillis);
            }
        }else{
            sequence = 0;
        }
        lastTimeMillis = currentTimeMillis;
        return ((currentTimeMillis - epoch) << (NODE_ID_BITS + SEQUENCE_BITS)) | (nodeId << SEQUENCE_BITS) | sequence;
    }

    private long waitNextMills(long currentTimestamp){
        while (currentTimestamp <= lastTimeMillis){
            currentTimestamp = System.currentTimeMillis();
        }
        return currentTimestamp;
    }

}
