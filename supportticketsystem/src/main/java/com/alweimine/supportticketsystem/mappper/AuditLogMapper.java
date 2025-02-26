package com.alweimine.supportticketsystem.mappper;

import com.alweimine.supportticketsystem.entities.AuditLog;
import com.alweimine.supportticketsystem.mappper.dto.AuditLogDto;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class AuditLogMapper extends AbstractMapper<AuditLog, AuditLogDto>{
    private UserMapper userMapper;
    @Override
    public AuditLog dtoToEntity(AuditLogDto dto) {

        return null;
    }

    @Override
    public AuditLogDto entityToDto(AuditLog entity) {
        AuditLogDto auditLogDto=new AuditLogDto();
        auditLogDto.setLogId(entity.getLogId());
        auditLogDto.setTimestamp(entity.getTimestamp());
        auditLogDto.setAction(entity.getAction());
        auditLogDto.setActionBy(userMapper.entityToDto(entity.getActionBy()));
        return auditLogDto;
    }
}
