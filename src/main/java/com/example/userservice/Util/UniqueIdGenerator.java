package com.example.userservice.Util;

public class UniqueIdGenerator
{
	private static final int kMAX = 1 << 16; // 65536
    private static int s_Counter = -1;
	
	// REF: GPT and https://gist.github.com/formix/d9521fd49cbeee305e2a
    public synchronized static String GenerateUniqueID()
    {
        if (s_Counter == -1)
        {
            s_Counter = new java.security.SecureRandom().nextInt(kMAX);
        }
        
        long timeMs = System.currentTimeMillis();
        long id = (timeMs << 16) | (s_Counter & 0xFFFF);
        s_Counter = (s_Counter + 1) % kMAX;
        return String.valueOf(id);
    }
}
